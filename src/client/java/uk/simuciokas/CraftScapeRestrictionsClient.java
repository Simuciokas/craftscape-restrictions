package uk.simuciokas;

import com.mojang.blaze3d.systems.RenderSystem;
import me.x150.renderer.render.Renderer3d;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import me.x150.renderer.event.RenderEvents;

import java.awt.*;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.Arrays;

public class CraftScapeRestrictionsClient implements ClientModInitializer {
	public static final String MOD_ID = "craftscape-restrictions";
	public static ArrayList<Chunk> CHUNK_LIST = new ArrayList<>(Arrays.asList(
			new Chunk(0, 0),
			new Chunk(1, 0),
			new Chunk(1, 1)
			));
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	@Override
	public void onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.
		RenderEvents.WORLD.register(this::world); //onWorldRender);
	}

	private void world(MatrixStack stack) {
		var client = MinecraftClient.getInstance();
		if (client.player != null) {
			var player = client.player;
			var pos = player.getChunkPos();
			var positiveChunk = new ChunkPos(pos.x+1, pos.z+1);
			var start = positiveChunk.getStartPos();

			for (var chunk: getNeighbouringChunks(pos.x, pos.z)) {
				if (!CHUNK_LIST.contains(chunk))
				{
					var chunkPos = new ChunkPos(chunk.x, chunk.z);
					var startPos = chunkPos.getStartPos();
					Renderer3d.renderEdged(stack, new Color(1f, 0f, 0f, 0.5f), Color.BLACK, new Vec3d(startPos.getX(), -64, startPos.getZ()), new Vec3d(16, 320, 16));
				}
			}


			Renderer3d.renderFilled(stack, Color.RED, new Vec3d(0, 1, 0), new Vec3d(1, 0.01, 1));

			//Renderer3d.renderEdged(stack, new Color(1f, 0f, 0f, 0.5f), Color.BLACK, new Vec3d(start.getX(), -64, start.getZ()), new Vec3d(16, 320, 16));

		}
	};

	private ArrayList<Chunk> getNeighbouringChunks(int x, int z) {
		ArrayList<Chunk> list = new ArrayList<>();
		list.add(new Chunk(x+1, z));
		list.add(new Chunk(x, z+1));
		list.add(new Chunk(x+1, z+1));
		list.add(new Chunk(x-1, z));
		list.add(new Chunk(x, z-1));
		list.add(new Chunk(x-1, z-1));
		list.add(new Chunk(x-1, z+1));
		list.add(new Chunk(x+1, z-1));
		return list;
	};

	private void onWorldRender(WorldRenderContext event) {

		var tessellator = Tessellator.getInstance();
		var pos = event.camera().getPos();
		var consumer = event.consumers();
		Vector3f posf = new Vector3f();
		posf.x = (float)pos.x;
		posf.y = (float)pos.y;
		posf.z = (float)pos.z;

		var stack = event.matrixStack();
		var matrix = stack.peek().getPositionMatrix();
		var buffer = tessellator.begin(VertexFormat.DrawMode.TRIANGLE_STRIP, VertexFormats.POSITION_COLOR);

		buffer.vertex(matrix, 0.0f, 0.0f, 0.0f).color(0.0f, 1.0f, 0.0f, 1.0f);
		buffer.vertex(matrix, 1.0f,  0.0f, 0.0f).color(0.0f, 1.0f, 0.0f, 1.0f);
		buffer.vertex(matrix,  0.5f, 1.0f, 0.0f).color(0.0f, 1.0f, 0.0f, 1.0f);
		//WorldRenderer.drawBox(matrix, 0, 0, 0, 1, 1 ,1, 0.0f, 1.0f, 0.0f, 1.0f);
		RenderSystem.setShader(GameRenderer::getPositionColorProgram);

		BufferRenderer.draw(buffer.end());


	}
}