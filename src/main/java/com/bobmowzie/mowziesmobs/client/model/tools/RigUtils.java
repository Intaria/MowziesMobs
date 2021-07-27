package com.bobmowzie.mowziesmobs.client.model.tools;

import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import software.bernie.geckolib3.core.processor.IBone;

public class RigUtils {
    public static Vector3d lerp(Vector3d v, Vector3d u, float alpha) {
        return new Vector3d(
                MathHelper.lerp(alpha, (float)v.getX(), (float) u.getX()),
                MathHelper.lerp(alpha, (float)v.getY(), (float) u.getY()),
                MathHelper.lerp(alpha, (float)v.getZ(), (float) u.getZ())
        );
    }

    public static Vector3d lerpAngles(Vector3d v, Vector3d u, float alpha) {
        return new Vector3d(
                Math.toRadians(MathHelper.interpolateAngle(alpha, (float) Math.toDegrees(v.getX()), (float) Math.toDegrees(u.getX()))),
                Math.toRadians(MathHelper.interpolateAngle(alpha, (float) Math.toDegrees(v.getY()), (float) Math.toDegrees(u.getY()))),
                Math.toRadians(MathHelper.interpolateAngle(alpha, (float) Math.toDegrees(v.getZ()), (float) Math.toDegrees(u.getZ())))
        );
    }

    public static Vector3d blendAngles(Vector3d v, Vector3d u, float alpha) {
        return new Vector3d(
                Math.toRadians(MathHelper.wrapDegrees(Math.toDegrees(v.getX()) * alpha + Math.toDegrees(u.getX()))),
                Math.toRadians(MathHelper.wrapDegrees(Math.toDegrees(v.getY()) * alpha + Math.toDegrees(u.getY()))),
                Math.toRadians(MathHelper.wrapDegrees(Math.toDegrees(v.getZ()) * alpha + Math.toDegrees(u.getZ())))
        );
    }

    public static class BoneTransform {
        private final Vector3d translation;
        private final Vector3d rotation;
        private final Vector3d scale;

        public BoneTransform(
                double tx, double ty, double tz,
                double rx, double ry, double rz,
                double sx, double sy, double sz
        ) {
            translation = new Vector3d(tx, ty, tz);
            rotation = new Vector3d(rx, ry, rz);
            scale = new Vector3d(sx, sy, sz);
        }

        public BoneTransform(Vector3d t, Vector3d r, Vector3d s) {
            translation = t;
            rotation = r;
            scale = s;
        }

        public BoneTransform blend(BoneTransform other, float alpha) {
            return new BoneTransform(
                    this.translation.scale(alpha).add(other.translation),
                    RigUtils.blendAngles(this.rotation, other.rotation, alpha),
                    this.scale.scale(alpha).add(other.scale)
            );
        }

        public void apply(IBone bone) {
            apply(bone, false);
        }

        public void apply(IBone bone, boolean mirrorX) {
            float mirror = mirrorX ? -1 : 1;
            bone.setPositionX(bone.getPositionX() + mirror * (float) translation.getX());
            bone.setPositionY(bone.getPositionY() + (float) translation.getY());
            bone.setPositionZ(bone.getPositionZ() + (float) translation.getZ());

            bone.setRotationX(bone.getRotationX() + (float) rotation.getX());
            bone.setRotationY(bone.getRotationY() + mirror * (float) rotation.getY());
            bone.setRotationZ(bone.getRotationZ() + mirror * (float) rotation.getZ());

            bone.setScaleX(bone.getScaleX() * (float) scale.getX());
            bone.setScaleY(bone.getScaleY() * (float) scale.getY());
            bone.setScaleZ(bone.getScaleZ() * (float) scale.getZ());
        }
    }

    public static class BlendShape3DEntry {
        private BoneTransform transform;
        private Vector3d direction;
        private float power;

        public BlendShape3DEntry(BoneTransform transform, Vector3d direction, float power) {
            this.transform = transform;
            this.direction = direction.normalize();
            this.power = power;
        }

        public double getWeight(Vector3d dir) {
            double dot = dir.normalize().dotProduct(direction.normalize());
            dot = Math.max(dot, 0);
            dot = Math.pow(dot, power);
            return dot;
        }

        public BoneTransform blend(BoneTransform other, float alpha) {
            return transform.blend(other, alpha);
        }
    }

    public static class BlendShape3D {
        private final BlendShape3DEntry[] entries;
        public BlendShape3D(BlendShape3DEntry[] entries) {
            this.entries = entries;
        }

        public void evaluate(IBone bone, Vector3d dir) {
            evaluate(bone, dir, false);
        }

        private double[] getWeights(Vector3d dir) {
            double[] weights = new double[entries.length];
            double[] dotProducts = new double[entries.length];
            double totalDotProduct = 0.0;
            for (int i = 0; i < entries.length; i++) {
                BlendShape3DEntry entry = entries[i];
                double dot = 1.0 - entry.getWeight(dir);
                if (dot > 0.0) {
                    totalDotProduct += 1.0 / dot;
                    dotProducts[i] = dot;
                }
                else {
                    weights[i] = 1.0;
                    return weights;
                }
            }
            for (int i = 0; i < entries.length; i++) {
                double dot_prod = totalDotProduct * dotProducts[i];
                if (dot_prod > 0) weights[i] = 1 / dot_prod;
                else weights[i] = 0.0;
            }
            return weights;
        }

        private double[] getWeightsGradientBand(Vector3d dir) {
            double[] weights = new double[entries.length];
            double[] sqrdDistances = new double[entries.length];
            double[] angularDistances = new double[entries.length];
            double totalSqrdDistance = 0.0;
            double totalAngularDistance = 0.0;
            for (int i = 0; i < entries.length; i++) {
                BlendShape3DEntry entry = entries[i];
                double sqrdDistance = dir.subtract(entry.direction).dotProduct(dir.subtract(entry.direction));
                if (sqrdDistance > 0.0) {
                    double angularDistance = -(MathHelper.clamp(dir.dotProduct(entry.direction), -1, 1) - 1) * 0.5;
                    totalSqrdDistance += 1.0 / sqrdDistance;
                    if (angularDistance > 0) totalAngularDistance += 1.0 / angularDistance;
                    sqrdDistances[i] = sqrdDistance;
                    angularDistances[i] = angularDistance;
                }
                else {
                    weights[i] = 1.0;
                    return weights;
                }
            }
            for (int i = 0; i < entries.length; i++) {
                double sqrdDistance = totalSqrdDistance * sqrdDistances[i];
                double angularDistance = totalAngularDistance * angularDistances[i];
                if (sqrdDistance > 0.0 && angularDistance > 0.0)
                    weights[i] = (1.0 / sqrdDistance) * 0.5 + (1.0 / angularDistance) * 0.5;
                else if (sqrdDistance > 0.0)
                    weights[i] = (1 / sqrdDistance) * 0.5 + 0.5;
                else weights[i] = 0.0;
            }
            return weights;
        }

        public void evaluate(IBone bone, Vector3d d, boolean mirrorX) {
            Vector3d dir = mirrorX ? d.mul(-1, 1, 1) : d;
            dir = dir.normalize();

            double[] weights = getWeights(dir);
            BoneTransform transform = new BoneTransform(
                    0.0, 0.0 ,0.0,
                    0.0, 0.0, 0.0,
                    0.0, 0.0, 0.0
            );
            for (int i = 0; i < entries.length; i++) {
                BlendShape3DEntry entry = entries[i];
                transform = entry.blend(transform, (float) MathHelper.clamp(weights[i],0, 1));
            }
            transform.apply(bone, mirrorX);
        }
    }
}
