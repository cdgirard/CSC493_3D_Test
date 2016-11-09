package com.my3d.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.utils.Array;

public class My3dGame extends ApplicationAdapter
{
	public Environment environment;
	public PerspectiveCamera cam;
	public CameraInputController camController;
	public ModelBatch modelBatch;
	public Model model;
	public ModelInstance modelInstance;
	public AssetManager assets;
	private AnimationController controller;
	

	@Override
	public void create()
	{
		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 04f, 0.4f, 1f));
		environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));
		modelBatch = new ModelBatch();
		
		cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		cam.position.set(0, 100f, 100f);
		cam.lookAt(0, 0, 0);
		cam.near = 1f;
		cam.far = 300f;
		cam.update();
		
		assets = new AssetManager();
		// assets.load("models/book/DH_book_00.g3db", Model.class);
		assets.load("models/zombie/zombie3.g3dj", Model.class);
		assets.finishLoading();
		// model = assets.get("models/book/DH_book_00.g3db", Model.class);
		model = assets.get("models/zombie/zombie3.g3dj", Model.class);
		
		modelInstance = new ModelInstance(model);
		
		controller = new AnimationController(modelInstance);
		controller.setAnimation("mixamo.com",-1);
		

		final BoundingBox box = model.calculateBoundingBox(new BoundingBox());
		Matrix4 transform = new Matrix4();
		//transform.scale(6f, 6f, 6f);
		box.mul(transform);
		camController = new CameraInputController(cam)
		{
			private final Vector3 position = new Vector3();
			
			@Override
			public boolean touchUp(int screenX, int screenY, int pointer, int button)
			{
				return super.touchUp(screenX, screenY, pointer, button);
			}
		};
		Gdx.input.setInputProcessor(camController);
			
	}

	@Override
	public void render()
	{
		camController.update();
		
		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		
		controller.update(Gdx.graphics.getDeltaTime());
		
		modelBatch.begin(cam);
		modelBatch.render(modelInstance, environment);
		modelBatch.end();
	}
	
	@Override
	public void dispose()
	{
		modelBatch.dispose();
		assets.dispose();
	}
}
