package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
public class MyGdxGame extends ApplicationAdapter {
    Stage stage;
    TextButton button;
    TextButton.TextButtonStyle textButtonStyle;
    BitmapFont font;
    Skin skin;
    TextureAtlas buttonAtlas;
    SpriteBatch batch;
    Sprite sprite;
    Texture img;
    World world;
    Body body;
    OrthographicCamera camera;
    final float PIXELS_TO_METERS = 100f;
    @Override
    public void create() {
        batch = new SpriteBatch();
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        font = new BitmapFont();
        skin = new Skin();
        buttonAtlas = new TextureAtlas("button.pack");
        img = new Texture("penguin.png");
        sprite = new Sprite(img);
        // Center the sprite in the top/middle of the screen
        sprite.setPosition(Gdx.graphics.getWidth() / 2 - sprite.getWidth() / 2,
                Gdx.graphics.getHeight() / 2);

        // Create a physics world, the heart of the simulation.  The Vector
        //passed in is gravity
        world = new World(new Vector2(0, -98f), true);

        // Now create a BodyDefinition.  This defines the physics objects type
        //and position in the simulation
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        // We are going to use 1 to 1 dimensions.  Meaning 1 in physics engine
        //is 1 pixel
        // Set our body to the same position as our sprite
        bodyDef.position.set(sprite.getX(), sprite.getY());

        // Create a body in the world using our definition
        body = world.createBody(bodyDef);

        // Now define the dimensions of the physics shape
        PolygonShape shape = new PolygonShape();
        // We are a box, so this makes sense, no?
        // Basically set the physics polygon to a box with the same dimensions
        //as our sprite
        shape.setAsBox(sprite.getWidth()/2, sprite.getHeight()/2);


        // Shape is the only disposable of the lot, so get rid of it
        shape.dispose();
        skin.addRegions(buttonAtlas);
        textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = font;
        textButtonStyle.up = skin.getDrawable("Button_Pressed");
        textButtonStyle.down = skin.getDrawable("Button_Released");
        textButtonStyle.checked = skin.getDrawable("Button_Pressed");
        button = new TextButton("", textButtonStyle);
        stage.addActor(button);
        camera = new OrthographicCamera(Gdx.graphics.getWidth(),Gdx.graphics.
                getHeight());
        button.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                //if button is pressed do stuff
                body.setLinearVelocity(100f,100f);
                System.out.println("button works");
            }
        });
    }


	@Override
	public void render () {
        camera.update();
        world.step(Gdx.graphics.getDeltaTime(), 6, 2);
        sprite.setPosition(body.getPosition().x, body.getPosition().y);

        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.draw(sprite, sprite.getX(), sprite.getY());
        batch.end();

        stage.draw();
	}
    @Override
    public void dispose() {
        img.dispose();
        world.dispose();
    }
}
