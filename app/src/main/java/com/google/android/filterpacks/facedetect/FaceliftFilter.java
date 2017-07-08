package com.google.android.filterpacks.facedetect;

import android.filterfw.core.FilterContext;
import android.filterfw.core.Frame;
import android.filterfw.core.FrameFormat;
import android.filterfw.core.GLFrame;
import android.filterfw.core.GenerateFinalPort;
import android.filterfw.core.ShaderProgram;
import android.filterfw.format.ImageFormat;
import android.graphics.Rect;
import android.opengl.GLES20;

public class FaceliftFilter extends FaceMaskEffectFilter {
    private static final int DEFAULT_ROW_BUFFERS = 2048;
    private ShaderProgram mColumnSmoothProgram;
    private final String mColumnSmoothShader;
    private ShaderProgram mCropProgram;
    @GenerateFinalPort(hasDefault = true, name = "glCoordOffset")
    private float mGlCoordOffset;
    private ShaderProgram mIntensityProgram;
    private final String mIntensityShader;
    @GenerateFinalPort(hasDefault = true, name = "padSize")
    private int mPadSize;
    @GenerateFinalPort(hasDefault = true, name = "rangeSigma")
    private float mRangeSigma;
    @GenerateFinalPort(hasDefault = true, name = "rangeSteps")
    private int mRangeSteps;
    private ShaderProgram mRgbToRgbaProgram;
    private final String mRgbToRgbaShader;
    private ShaderProgram mRowSmoothProgram;
    private final String mRowSmoothShader;
    private int mSliceSize;
    private Frame mTableFrame;
    private Frame mTanhFrame;

    private void processOneSlice(android.filterfw.core.FilterContext r1, int r2, int r3, int r4, int r5, android.filterfw.core.Frame r6, android.filterfw.core.Frame r7, android.filterfw.core.Frame r8) {
        /* JADX: method processing error */
/*
        Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: com.google.android.filterpacks.facedetect.FaceliftFilter.processOneSlice(android.filterfw.core.FilterContext, int, int, int, int, android.filterfw.core.Frame, android.filterfw.core.Frame, android.filterfw.core.Frame):void
	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:113)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:256)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:281)
	at jadx.api.JavaClass.decompile(JavaClass.java:59)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:161)
Caused by: jadx.core.utils.exceptions.DecodeException:  in method: com.google.android.filterpacks.facedetect.FaceliftFilter.processOneSlice(android.filterfw.core.FilterContext, int, int, int, int, android.filterfw.core.Frame, android.filterfw.core.Frame, android.filterfw.core.Frame):void
	at jadx.core.dex.instructions.InsnDecoder.decodeInsns(InsnDecoder.java:46)
	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:98)
	... 5 more
Caused by: java.lang.IllegalArgumentException: bogus opcode: 00e3
	at com.android.dx.io.OpcodeInfo.get(OpcodeInfo.java:1197)
	at com.android.dx.io.OpcodeInfo.getFormat(OpcodeInfo.java:1212)
	at com.android.dx.io.instructions.DecodedInstruction.decode(DecodedInstruction.java:72)
	at jadx.core.dex.instructions.InsnDecoder.decodeInsns(InsnDecoder.java:43)
	... 6 more
*/
        /*
        // Can't load method instructions.
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.filterpacks.facedetect.FaceliftFilter.processOneSlice(android.filterfw.core.FilterContext, int, int, int, int, android.filterfw.core.Frame, android.filterfw.core.Frame, android.filterfw.core.Frame):void");
    }

    public FaceliftFilter(String name) {
        super(name);
        this.mRangeSteps = 10;
        this.mRangeSigma = 0.01f;
        this.mPadSize = 10;
        this.mGlCoordOffset = 0.5f;
        this.mRgbToRgbaShader = "precision mediump float;\nuniform sampler2D tex_sampler_0;\nuniform vec2 disp;\nvarying vec2 v_texcoord;\nvoid main() {\n  const vec3 weights = vec3(0.299, 0.587, 0.114);\n  vec3 color = 2.0 * texture2D(tex_sampler_0, v_texcoord).rgb;\n  color -= 0.5 * texture2D(tex_sampler_0, (v_texcoord + vec2(0, disp.y))).rgb;\n  color -= 0.5 *texture2D(tex_sampler_0, (v_texcoord + vec2(disp.x, 0))).rgb;\n  gl_FragColor = vec4(color, dot(weights, color));\n}\n";
        this.mIntensityShader = "precision mediump float;\nuniform sampler2D tex_sampler_0;\nuniform sampler2D tex_sampler_1;\nuniform float tbl_scale;\nuniform float tbl_offset;\nuniform float coord_offset;\nuniform float tex_offset;\nuniform float tex_scale;\nuniform float rangeSteps;\nuniform float k_scale;\nvarying vec2 v_texcoord;\nvoid main() {\n  float k_value = mod(gl_FragCoord.y - coord_offset, rangeSteps);\n  vec2 coord;\n  coord.x = v_texcoord.x;\n  coord.y = tex_offset + tex_scale * (gl_FragCoord.y - coord_offset - k_value);\n  vec4 color = texture2D(tex_sampler_0, coord);\n  k_value *= k_scale;\n  coord.x = tbl_offset + tbl_scale * abs(k_value - color.a);\n  coord.y = 0.5;\n  vec4 weight_byte = texture2D(tex_sampler_1, coord);\n  float weight = weight_byte.g + 0.00390625 * weight_byte.r;\n  gl_FragColor = vec4(color.rgb, weight);\n}\n";
        this.mColumnSmoothShader = "precision highp float;\nuniform sampler2D tex_sampler_0;\nuniform float stepsize;\nvarying vec2 v_texcoord;\nvoid main() {\n  vec4 color = texture2D(tex_sampler_0, v_texcoord);\n  vec4 result = vec4(color.a * color.rgb, color.a);\n  vec2 coord = vec2(0.0, stepsize);\n  color = texture2D(tex_sampler_0, v_texcoord + coord * 6.0);\n  result += vec4(color.a * color.rgb, color.a);\n  color = texture2D(tex_sampler_0, v_texcoord + coord * 4.0);\n  result += vec4(color.a * color.rgb, color.a);\n  color = texture2D(tex_sampler_0, v_texcoord + coord * 2.0);\n  result += vec4(color.a * color.rgb, color.a);\n  color = texture2D(tex_sampler_0, v_texcoord - coord * 2.0);\n  result += vec4(color.a * color.rgb, color.a);\n  color = texture2D(tex_sampler_0, v_texcoord - coord * 4.0);\n  result += vec4(color.a * color.rgb, color.a);\n  color = texture2D(tex_sampler_0, v_texcoord - coord * 6.0);\n  result += vec4(color.a * color.rgb, color.a);\n  result.rgb = result.rgb / result.a;\n  gl_FragColor = vec4(result.rgb, result.a / 6.0);\n}\n";
        this.mRowSmoothShader = "precision highp float;\nconst mat4 coeff_yuv = mat4(0.299, -0.168736,  0.5,      0.000, 0.587, -0.331264, -0.418688, 0.000, 0.114,  0.5,      -0.081312, 0.000, 0.000,  0.5,       0.5,      1.000 );\nconst float byte_scale = 0.00390625;\nuniform sampler2D tex_sampler_0;\nuniform sampler2D tex_sampler_1;\nuniform sampler2D tex_sampler_2;\nuniform vec3 yuv_mean;\nuniform mat3 yuv_var;\nuniform vec3 yuv_scale;\nuniform float tanh_offset;\nuniform float tanh_scale;\nuniform float row_offset;\nuniform float tbl_offset;\nuniform float range_scale;\nuniform float stepsize;\nvarying vec2 v_texcoord;\nvoid main() {\n  vec4 rgba = texture2D(tex_sampler_0, v_texcoord);\n  vec4 yuv = coeff_yuv * vec4(rgba.rgb, 1.0);\n  vec3 proj = yuv_var * (yuv.xyz - yuv_mean);\n  proj = yuv_scale * proj;\n  float dist = sqrt(0.3333 * dot(proj, proj));\n  dist = min(1.0, dist * 0.2);\n  float tanh_value = tanh_offset + tanh_scale * dist;\n  vec4 weight_byte = texture2D(tex_sampler_2, vec2(tanh_value, 0.5));\n  float alpha = weight_byte.g +  byte_scale * weight_byte.r;\n  float k_value = rgba.a * range_scale;\n  vec2 coord = v_texcoord;\n  coord.y = tbl_offset + v_texcoord.y - row_offset + k_value;\n  vec4 color = texture2D(tex_sampler_1, coord);\n  vec4 result = vec4(color.a * color.rgb, color.a);\n  vec2 disp = vec2(stepsize, 0.0);\n  color =  texture2D(tex_sampler_1, coord + disp * 5.5);\n  result += vec4(color.a * color.rgb, color.a);\n  color =  texture2D(tex_sampler_1, coord + disp * 3.5);\n  result += vec4(color.a * color.rgb, color.a);\n  color =  texture2D(tex_sampler_1, coord + disp * 1.5);\n  result += vec4(color.a * color.rgb, color.a);\n  color =  texture2D(tex_sampler_1, coord - disp * 1.5);\n  result += vec4(color.a * color.rgb, color.a);\n  color =  texture2D(tex_sampler_1, coord - disp * 3.5);\n  result += vec4(color.a * color.rgb, color.a);\n  color =  texture2D(tex_sampler_1, coord - disp * 5.5);\n  result += vec4(color.a * color.rgb, color.a);\n  result.rgb = result.rgb / result.a;\n  gl_FragColor = vec4(result.rgb, alpha);\n}\n";
        this.mTableFrame = null;
        this.mTanhFrame = null;
    }

    public void tearDown(FilterContext context) {
        if (this.mTableFrame != null) {
            this.mTableFrame.release();
            this.mTableFrame = null;
        }
        if (this.mTanhFrame != null) {
            this.mTanhFrame.release();
            this.mTanhFrame = null;
        }
    }

    protected void initPrograms(FilterContext context) {
        this.mIntensityProgram = new ShaderProgram(context, "precision mediump float;\nuniform sampler2D tex_sampler_0;\nuniform sampler2D tex_sampler_1;\nuniform float tbl_scale;\nuniform float tbl_offset;\nuniform float coord_offset;\nuniform float tex_offset;\nuniform float tex_scale;\nuniform float rangeSteps;\nuniform float k_scale;\nvarying vec2 v_texcoord;\nvoid main() {\n  float k_value = mod(gl_FragCoord.y - coord_offset, rangeSteps);\n  vec2 coord;\n  coord.x = v_texcoord.x;\n  coord.y = tex_offset + tex_scale * (gl_FragCoord.y - coord_offset - k_value);\n  vec4 color = texture2D(tex_sampler_0, coord);\n  k_value *= k_scale;\n  coord.x = tbl_offset + tbl_scale * abs(k_value - color.a);\n  coord.y = 0.5;\n  vec4 weight_byte = texture2D(tex_sampler_1, coord);\n  float weight = weight_byte.g + 0.00390625 * weight_byte.r;\n  gl_FragColor = vec4(color.rgb, weight);\n}\n");
        this.mRgbToRgbaProgram = new ShaderProgram(context, "precision mediump float;\nuniform sampler2D tex_sampler_0;\nuniform vec2 disp;\nvarying vec2 v_texcoord;\nvoid main() {\n  const vec3 weights = vec3(0.299, 0.587, 0.114);\n  vec3 color = 2.0 * texture2D(tex_sampler_0, v_texcoord).rgb;\n  color -= 0.5 * texture2D(tex_sampler_0, (v_texcoord + vec2(0, disp.y))).rgb;\n  color -= 0.5 *texture2D(tex_sampler_0, (v_texcoord + vec2(disp.x, 0))).rgb;\n  gl_FragColor = vec4(color, dot(weights, color));\n}\n");
        this.mColumnSmoothProgram = new ShaderProgram(context, "precision highp float;\nuniform sampler2D tex_sampler_0;\nuniform float stepsize;\nvarying vec2 v_texcoord;\nvoid main() {\n  vec4 color = texture2D(tex_sampler_0, v_texcoord);\n  vec4 result = vec4(color.a * color.rgb, color.a);\n  vec2 coord = vec2(0.0, stepsize);\n  color = texture2D(tex_sampler_0, v_texcoord + coord * 6.0);\n  result += vec4(color.a * color.rgb, color.a);\n  color = texture2D(tex_sampler_0, v_texcoord + coord * 4.0);\n  result += vec4(color.a * color.rgb, color.a);\n  color = texture2D(tex_sampler_0, v_texcoord + coord * 2.0);\n  result += vec4(color.a * color.rgb, color.a);\n  color = texture2D(tex_sampler_0, v_texcoord - coord * 2.0);\n  result += vec4(color.a * color.rgb, color.a);\n  color = texture2D(tex_sampler_0, v_texcoord - coord * 4.0);\n  result += vec4(color.a * color.rgb, color.a);\n  color = texture2D(tex_sampler_0, v_texcoord - coord * 6.0);\n  result += vec4(color.a * color.rgb, color.a);\n  result.rgb = result.rgb / result.a;\n  gl_FragColor = vec4(result.rgb, result.a / 6.0);\n}\n");
        this.mRowSmoothProgram = new ShaderProgram(context, "precision highp float;\nconst mat4 coeff_yuv = mat4(0.299, -0.168736,  0.5,      0.000, 0.587, -0.331264, -0.418688, 0.000, 0.114,  0.5,      -0.081312, 0.000, 0.000,  0.5,       0.5,      1.000 );\nconst float byte_scale = 0.00390625;\nuniform sampler2D tex_sampler_0;\nuniform sampler2D tex_sampler_1;\nuniform sampler2D tex_sampler_2;\nuniform vec3 yuv_mean;\nuniform mat3 yuv_var;\nuniform vec3 yuv_scale;\nuniform float tanh_offset;\nuniform float tanh_scale;\nuniform float row_offset;\nuniform float tbl_offset;\nuniform float range_scale;\nuniform float stepsize;\nvarying vec2 v_texcoord;\nvoid main() {\n  vec4 rgba = texture2D(tex_sampler_0, v_texcoord);\n  vec4 yuv = coeff_yuv * vec4(rgba.rgb, 1.0);\n  vec3 proj = yuv_var * (yuv.xyz - yuv_mean);\n  proj = yuv_scale * proj;\n  float dist = sqrt(0.3333 * dot(proj, proj));\n  dist = min(1.0, dist * 0.2);\n  float tanh_value = tanh_offset + tanh_scale * dist;\n  vec4 weight_byte = texture2D(tex_sampler_2, vec2(tanh_value, 0.5));\n  float alpha = weight_byte.g +  byte_scale * weight_byte.r;\n  float k_value = rgba.a * range_scale;\n  vec2 coord = v_texcoord;\n  coord.y = tbl_offset + v_texcoord.y - row_offset + k_value;\n  vec4 color = texture2D(tex_sampler_1, coord);\n  vec4 result = vec4(color.a * color.rgb, color.a);\n  vec2 disp = vec2(stepsize, 0.0);\n  color =  texture2D(tex_sampler_1, coord + disp * 5.5);\n  result += vec4(color.a * color.rgb, color.a);\n  color =  texture2D(tex_sampler_1, coord + disp * 3.5);\n  result += vec4(color.a * color.rgb, color.a);\n  color =  texture2D(tex_sampler_1, coord + disp * 1.5);\n  result += vec4(color.a * color.rgb, color.a);\n  color =  texture2D(tex_sampler_1, coord - disp * 1.5);\n  result += vec4(color.a * color.rgb, color.a);\n  color =  texture2D(tex_sampler_1, coord - disp * 3.5);\n  result += vec4(color.a * color.rgb, color.a);\n  color =  texture2D(tex_sampler_1, coord - disp * 5.5);\n  result += vec4(color.a * color.rgb, color.a);\n  result.rgb = result.rgb / result.a;\n  gl_FragColor = vec4(result.rgb, alpha);\n}\n");
    }

    private void initParameters(FilterContext context) {
        if (this.mTableFrame != null) {
            this.mTableFrame.release();
        }
        this.mTableFrame = createExpTable(context);
        if (this.mTanhFrame != null) {
            this.mTanhFrame.release();
        }
        this.mTanhFrame = createTanhTable(context, 500, 5.0f, 1.15f, 2.5f);
        this.mRgbToRgbaProgram.setHostValue("disp", new float[]{1.0f / ((float) this.mWidth), 1.0f / ((float) this.mHeight)});
        this.mIntensityProgram.setHostValue("k_scale", Float.valueOf(1.0f / (((float) this.mRangeSteps) - 1.0f)));
        this.mIntensityProgram.setHostValue("coord_offset", Float.valueOf(this.mGlCoordOffset));
        this.mIntensityProgram.setHostValue("rangeSteps", Float.valueOf((float) this.mRangeSteps));
        this.mIntensityProgram.setHostValue("tbl_offset", Float.valueOf(0.001953125f));
        this.mIntensityProgram.setHostValue("tbl_scale", Float.valueOf(255.0f / 256.0f));
        this.mRowSmoothProgram.setHostValue("stepsize", Float.valueOf(1.0f / ((float) this.mWidth)));
        this.mRowSmoothProgram.setHostValue("tanh_offset", Float.valueOf(0.001f));
        this.mRowSmoothProgram.setHostValue("tanh_scale", Float.valueOf(499.0f / 500.0f));
        this.mSliceSize = ((int) Math.floor((double) (DEFAULT_ROW_BUFFERS / this.mRangeSteps))) - (this.mPadSize * 2);
    }

    private Frame createExpTable(FilterContext context) {
        int[] array = new int[256];
        float scale = 1.5378702E-5f / this.mRangeSigma;
        for (int i = 0; i < 256; i++) {
            array[i] = (int) (65535.0f * ((float) Math.exp((double) (((-scale) * ((float) i)) * ((float) i)))));
            if (array[i] < 256) {
                array[i] = 256;
            }
        }
        Frame tableFrame = context.getFrameManager().newFrame(ImageFormat.create(256, 1, 3, 3));
        tableFrame.setInts(array);
        return tableFrame;
    }

    private Rect rescaleRect(Frame input, Rect rect, float ratio) {
        return createBoundedRect(input, new Rect(rect.centerX() - ((int) ((((double) ratio) * 0.5d) * ((double) rect.width()))), rect.centerY() - ((int) ((((double) ratio) * 0.5d) * ((double) rect.height()))), rect.centerX() + ((int) ((((double) ratio) * 0.5d) * ((double) rect.width()))), rect.centerY() + ((int) ((((double) ratio) * 0.5d) * ((double) rect.height())))));
    }

    public void process(FilterContext context) {
        Frame input = pullInput("image");
        FrameFormat inputFormat = input.getFormat();
        FaceMeta faces = (FaceMeta) pullInput("faces").getObjectValue();
        int faceCount = faces.count();
        if (this.mIdentityProgram == null || this.mColorPcaProgram == null || this.mRgbToYuvProgram == null || this.mIntensityProgram == null || this.mRgbToRgbaProgram == null || this.mColumnSmoothProgram == null || this.mRowSmoothProgram == null) {
            throw new RuntimeException("programs are missing at process.");
        }
        Frame yuvFrame = context.getFrameManager().newFrame(inputFormat);
        this.mRgbToYuvProgram.process(input, yuvFrame);
        Frame tempFrame = context.getFrameManager().newFrame(inputFormat);
        Frame output = context.getFrameManager().newFrame(inputFormat);
        ((GLFrame) output).focus();
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        GLES20.glClear(16384);
        if (!(inputFormat.getWidth() == this.mWidth && inputFormat.getHeight() == this.mHeight)) {
            this.mWidth = inputFormat.getWidth();
            this.mHeight = inputFormat.getHeight();
            initParameters(context);
        }
        float[] color_mean = new float[3];
        float[] eigen_values = new float[3];
        float[] eigen_vectors = new float[9];
        Rect faceRect = new Rect();
        for (int i = 0; i < faceCount; i++) {
            faceRect.left = Math.round(((float) this.mWidth) * faces.getFaceX0(i));
            faceRect.top = Math.round(((float) this.mHeight) * faces.getFaceY0(i));
            faceRect.right = Math.round(((float) this.mWidth) * faces.getFaceX1(i));
            faceRect.bottom = Math.round(((float) this.mHeight) * faces.getFaceY1(i));
            computeFaceColorPCA(context, yuvFrame, faceRect, color_mean, eigen_values, eigen_vectors);
            this.mRowSmoothProgram.setHostValue("yuv_mean", color_mean);
            this.mRowSmoothProgram.setHostValue("yuv_var", eigen_vectors);
            this.mRowSmoothProgram.setHostValue("yuv_scale", new float[]{1.0f / eigen_values[0], 1.0f / eigen_values[1], 1.0f / eigen_values[2]});
            Rect smoothRect = rescaleRect(input, faceRect, 1.5f);
            int startIdx = smoothRect.top;
            while (startIdx < smoothRect.bottom) {
                int endIdx = (this.mSliceSize + startIdx) - 1;
                if (endIdx > smoothRect.bottom) {
                    endIdx = smoothRect.bottom;
                }
                processOneSlice(context, smoothRect.left, smoothRect.right, startIdx, endIdx, input, tempFrame, output);
                startIdx += this.mSliceSize;
            }
        }
        pushOutput("image", output);
        output.release();
        yuvFrame.release();
        tempFrame.release();
    }
}
