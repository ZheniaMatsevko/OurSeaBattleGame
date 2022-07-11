package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.ArrayList;

/**
 * Даний клас реалізовує графічний інтерфейс та функції екрану налаштувань
 */
public class OptionsScreen extends ScreenAdapter implements InputProcessor {

    private SeaBattleGame game;
    private BitmapFont font;
    private BitmapFont fontBig;
    private Stage stage;
    private SpriteBatch batch;
    private Sprite sprite;
    private ImageButton menu;
    private Image delayUp;
    private Image delayDown;
    private int level;
    private int bonusScore;
    private float delay;
    private Image fullMusic;
    private int musicStage = 2;
    private boolean soundState = true;
    private Label sound;


    public OptionsScreen(SeaBattleGame game, int level, int bonusScore, int musicStage, boolean soundState)
    {

        this.bonusScore = bonusScore;
        this.level = level;
        this.game = game;
        this.musicStage = musicStage;
        this.soundState = soundState;
        delay = game.delay;

        stage = new Stage(new ScreenViewport());
        batch = new SpriteBatch();
        /**
         * кнопка повернення в меню
         */
        Texture menuTexture = new Texture(Gdx.files.internal("menu.png"));
        TextureRegion menuTextureRegion = new TextureRegion(menuTexture);
        TextureRegionDrawable menuTexRegionDrawable = new TextureRegionDrawable(menuTextureRegion);
        menu = new ImageButton(menuTexRegionDrawable);
        menu.setPosition(Gdx.graphics.getWidth()-menu.getWidth()-15,Gdx.graphics.getHeight()-menu.getHeight()-15);
        stage.addActor(menu);


        delayUp = new Image(new Texture(Gdx.files.internal("delayUp.png")));
        delayUp.setPosition(Gdx.graphics.getWidth()/2 + 250,Gdx.graphics.getHeight()-220);
        stage.addActor(delayUp);

        delayDown = new Image(new Texture(Gdx.files.internal("delayDown.png")));
        delayDown.setPosition(Gdx.graphics.getWidth()/2 + 250,Gdx.graphics.getHeight()-260);
        stage.addActor(delayDown);

        if(musicStage==2) {
            fullMusic = new Image(new Texture(Gdx.files.internal("fullMusic.png")));
        }
        else if(musicStage==1){
            fullMusic = new Image(new Texture(Gdx.files.internal("lowMusic.png")));

        }
        else if (musicStage==0) {

                fullMusic = new Image(new Texture(Gdx.files.internal("offMusic.png")));


        }
            fullMusic.setPosition(Gdx.graphics.getWidth() / 2 - 130, Gdx.graphics.getHeight() - 350);
        stage.addActor(fullMusic);
        /**
         * текст
         */
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("ukr.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 40;
        parameter.color = Color.WHITE;
        parameter.borderWidth = 2;
        parameter.borderColor = Color.BLACK;
        font = generator.generateFont(parameter);
        Label.LabelStyle style = new Label.LabelStyle();
        style.font = font;



        FreeTypeFontGenerator generatorBig = new FreeTypeFontGenerator(Gdx.files.internal("ukr.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameterBig = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameterBig.size = 50;
        parameterBig.color = Color.WHITE;
        parameterBig.borderWidth = 3;
        parameterBig.borderColor = Color.BLACK;
        fontBig = generatorBig.generateFont(parameterBig);

        style.font = font;
        sound = new Label("Sound effects: ON",style);
        if(soundState==false) sound.setText("Sound effects: OFF");
        sound.setPosition(Gdx.graphics.getWidth()/2 - 300,Gdx.graphics.getHeight()-460);
        stage.addActor(sound);

        sprite = new Sprite(new Texture(Gdx.files.internal("options.jpg")));

        sprite.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.input.setInputProcessor(this);

    }

    @Override
    public void show(){}

    /**
     * Малюємо екран налаштувань
     * @param delta
     */
    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);
        batch.begin();
        sprite.draw(batch);

        fontBig.draw(batch,"Settings",Gdx.graphics.getWidth()/2 - 150,Gdx.graphics.getHeight()-20);

        font.draw(batch,"Computer delay: " + delay,Gdx.graphics.getWidth()/2 - 300,Gdx.graphics.getHeight()-200);
        font.draw(batch,"Music: ",Gdx.graphics.getWidth()/2 - 300,Gdx.graphics.getHeight()-310);
     //   font.draw(batch,,Gdx.graphics.getWidth()/2 - 300,Gdx.graphics.getHeight()-420);

        batch.end();
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }
    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    /**
     * Відбувається дія при натисканні на екран лівою кнопкою миші
     */
    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Vector2 coord = stage.screenToStageCoordinates(new Vector2((float)screenX,(float) screenY));
        Actor hitActor = stage.hit(coord.x,coord.y,true);


        if(hitActor==menu.getImage()){
            if(game.soundState)  game.clicksound.play();

            System.out.println("Hit " + hitActor.getClass());
            game.setScreen(new MainMenu(game,level,bonusScore));
        }
        else if(hitActor==delayUp) {
            if(delay<5) {
                delay++;
                game.delay = this.delay;
                if(game.soundState)  game.clicksound.play();}
        }
        else if(hitActor==delayDown) {
            if(delay>1) {
                delay--;
                game.delay = this.delay;
                if(game.soundState)  game.clicksound.play();}
        }
        else if(hitActor==fullMusic) {
            if(game.soundState)  game.clicksound.play();
            if(musicStage==2) {
                fullMusic.setDrawable(new SpriteDrawable(new Sprite(new Texture(Gdx.files.internal("lowMusic.png")))));
                musicStage--;
                game.musicLow();
            }
             else if(musicStage==1) {
                fullMusic.setDrawable(new SpriteDrawable(new Sprite(new Texture(Gdx.files.internal("offMusic.png")))));
                musicStage--;
                game.musicOff();
            }
            else if(musicStage==0) {
                fullMusic.setDrawable(new SpriteDrawable(new Sprite(new Texture(Gdx.files.internal("fullMusic.png")))));
                musicStage=2;
                game.musicOn();

            }
        }
        else if(hitActor==sound) {
            soundState=!soundState;
            if(soundState) {
                sound.setText("Sound effects: ON");
                game.soundOn();
            }
            else {
                sound.setText("Sound effects: OFF");
                game.soundOff();
            }
        }

        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }

}
