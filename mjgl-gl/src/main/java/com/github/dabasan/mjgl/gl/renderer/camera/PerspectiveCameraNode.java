package com.github.dabasan.mjgl.gl.renderer.camera;

import java.util.List;

import com.github.dabasan.ejml_3dtools.Matrix;
import com.github.dabasan.mjgl.gl.shader.ShaderProgram;
import com.github.dabasan.mjgl.math.MathFunctions;

/**
 * Perspective camera
 * 
 * @author Daba
 *
 */
public class PerspectiveCameraNode extends CameraNode {
	private double fov;

	public PerspectiveCameraNode() {
		fov = MathFunctions.convDegToRad(60.0);
	}

	public void setFOV(double fov) {
		this.fov = fov;
	}
	public double getFOV() {
		return fov;
	}

	@Override
	public void update(List<ShaderProgram> programs) {
		super.update(programs);

		Matrix viewTransformation = MatrixFunctions.getViewTransformationMatrix(this.getPosition(),
				this.getTarget(), this.getUp());
		Matrix projection = MatrixFunctions.getPerspectiveMatrix(fov, this.getAspect(),
				this.getNear(), this.getFar());

		Matrix vp = projection.mult(viewTransformation);

		// Transfer vp to shader programs.
	}
}
