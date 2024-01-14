package net.coderbot.iris.compat.dh.mixin;

import com.seibel.distanthorizons.core.render.renderer.shaders.AbstractShaderRenderer;
import com.seibel.distanthorizons.core.render.renderer.shaders.DhApplyShader;
import com.seibel.distanthorizons.core.render.renderer.shaders.FogShader;
import com.seibel.distanthorizons.core.render.renderer.shaders.SSAOApplyShader;
import com.seibel.distanthorizons.core.wrapperInterfaces.minecraft.IMinecraftRenderWrapper;
import net.coderbot.iris.Iris;
import net.coderbot.iris.compat.dh.DHCompat;
import net.coderbot.iris.compat.dh.DHCompatInternal;
import net.coderbot.iris.pipeline.WorldRenderingPipeline;
import net.coderbot.iris.pipeline.newshader.NewWorldRenderingPipeline;
import net.irisshaders.iris.api.v0.IrisApi;
import org.lwjgl.opengl.GL32;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = {
	DhApplyShader.class,
	SSAOApplyShader.class,
	FogShader.class
}, remap = false)
public class MixinDHApplyShader extends AbstractShaderRenderer {
	@Inject(method = "onRender", at = @At("HEAD"), cancellable = true)
	private void onRender2(CallbackInfo ci) {
		if (DHCompatInternal.INSTANCE.shouldOverride) ci.cancel();
	}

	@Override
	public void render(float partialTicks) {
		if (DHCompatInternal.INSTANCE.shouldOverride) return;

		this.init();
		this.shader.bind();
		this.onApplyUniforms(partialTicks);
		int width = MC_RENDER.getTargetFrameBufferViewportWidth();
		int height = MC_RENDER.getTargetFrameBufferViewportHeight();
		GL32.glViewport(0, 0, width, height);
		this.onRender();
		this.shader.unbind();
	}
}
