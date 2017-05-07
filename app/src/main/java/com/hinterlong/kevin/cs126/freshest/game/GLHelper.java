package com.hinterlong.kevin.cs126.freshest.game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.util.Log;

/**
 * GL Utility functions taken from https://developer.android.com/training/graphics/opengl/draw.html
 */
public class GLHelper {
	public static final String TAG = GLHelper.class.getSimpleName();

	private GLHelper() {

	}

	/**
	 * Utility method for compiling a OpenGL shader.
	 * <p>
	 * <p><strong>Note:</strong> When developing shaders, use the checkGlError()
	 * method to debug shader coding errors.</p>
	 *
	 * @param type       - Vertex or fragment shader type.
	 * @param shaderCode - String containing the shader code.
	 * @return - Returns an id for the shader.
	 */
	public static int loadShader(int type, String shaderCode) {

		// create a vertex shader type (GLES20.GL_VERTEX_SHADER)
		// or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
		int shader = GLES20.glCreateShader(type);

		// add the source code to the shader and compile it
		GLES20.glShaderSource(shader, shaderCode);
		GLES20.glCompileShader(shader);

		return shader;
	}

	/**
	 * Utility method for debugging OpenGL calls. Provide the name of the call
	 * just after making it:
	 * <p>
	 * <pre>
	 * mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");
	 * MyGLRenderer.checkGlError("glGetUniformLocation");</pre>
	 * <p>
	 * If the operation is not successful, the check throws an error.
	 *
	 * @param glOperation - Name of the OpenGL call to check.
	 */
	public static void checkGlError(String glOperation) {
		int error;
		while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
			Log.e(TAG, glOperation + ": glError " + error);
			throw new RuntimeException(glOperation + ": glError " + error);
		}
	}

	/**
	 * loads a texture from bitmap
	 * http://www.learnopengles.com/android-lesson-four-introducing-basic-texturing/
	 *
	 * @param context    application context
	 * @param resourceId id of bitmap to be loaded
	 * @return result code of GLES20 call
	 */
	public static int loadTexture(Context context, int resourceId) {
		final int[] textureHandle = new int[1];

		GLES20.glGenTextures(1, textureHandle, 0);

		if (textureHandle[0] != 0) {
			final BitmapFactory.Options options = new BitmapFactory.Options();
			options.inScaled = false;   // No pre-scaling

			// Read in the resource
			Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resourceId, options);

			// Bind to the texture in OpenGL
			GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandle[0]);

			// Set filtering
			GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
			GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);

			// Load the bitmap into the bound texture.
			GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);

			// Recycle the bitmap, since its data has been loaded into OpenGL.
			bitmap.recycle();
		} else {
			throw new RuntimeException("Error loading texture.");
		}

		return textureHandle[0];
	}
}
