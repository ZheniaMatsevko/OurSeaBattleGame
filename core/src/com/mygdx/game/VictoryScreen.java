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
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class VictoryScreen extends ScreenAdapter implements InputProcessor {

    private SeaBattleGame game;
    private BitmapFont font;
    private BitmapFont fontBig;
    private Stage stage;
    private SpriteBatch batch;
    private Sprite sprite;
    private ImageButton restart;

    public VictoryScreen(SeaBattleGame game) {
        this.game = game;
        stage = new Stage(new ScreenViewport());
        batch = new SpriteBatch();

        Texture restartTexture = new Texture(Gdx.files.internal("restart3.png"));
        TextureRegion restartTextureRegion = new TextureRegion(restartTexture);
        TextureRegionDrawable restartTexRegionDrawable = new TextureRegionDrawable(restartTextureRegion);
        restart = new ImageButton(restartTexRegionDrawable);
        restart.setPosition(Gdx.graphics.getWidth()/2-restart.getWidth()/2-15,Gdx.graphics.getHeight()-restart.getHeight()-490);
        stage.addActor(restart);

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Zyana.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 50;
        parameter.color = Color.WHITE;
        parameter.borderWidth = 4;
        parameter.borderColor = Color.BLACK;
        font = generator.generateFont(parameter);


        FreeTypeFontGenerator generatorBig = new FreeTypeFontGenerator(Gdx.files.internal("font2.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameterBig = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameterBig.size = 60;
        parameterBig.color = Color.WHITE;
        parameterBig.borderWidth = 5;
        parameterBig.borderColor = Color.BLACK;
        fontBig = generatorBig.generateFont(parameterBig);

        sprite = new Sprite(new Texture(Gdx.files.internal("winBack.jpg")));

        sprite.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.input.setInputProcessor(this);

    }

    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);
        batch.begin();
        sprite.draw(batch);

        fontBig.draw(batch,"Congratulations!",Gdx.graphics.getWidth()/2 - 265,Gdx.graphics.getHeight()-20);
        font.draw(batch,"You won the game!",Gdx.graphics.getWidth()/2-220,Gdx.graphics.getHeight()-130);
        font.draw(batch,"Total score: " + game.getTotalScore(),Gdx.graphics.getWidth()/2-215,Gdx.graphics.getHeight()-190);
        font.draw(batch,"Bombs used: " + game.getBombsUsed(),Gdx.graphics.getWidth()/2-215,Gdx.graphics.getHeight()-250);
        font.draw(batch,"Radars used: " + game.getRadarsUsed(),Gdx.graphics.getWidth()/2-215,Gdx.graphics.getHeight()-310);
        font.draw(batch,"Your ships killed: " + game.getYourShipsKilled(),Gdx.graphics.getWidth()/2-215,Gdx.graphics.getHeight()-370);

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

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {

        Vector2 coord = stage.screenToStageCoordinates(new Vector2((float) screenX, (float) screenY));
        Actor hitActor = stage.hit(coord.x, coord.y, true);


        if (hitActor == restart.getImage()) {
            System.out.println("Hit " + hitActor.getClass());
            game.setScreen(new MainMenu(game, 1,0));
            game.setTotalScore(0);
            game.setRadarsUsed(0);
            game.setBombsUsed(0);
            game.setYourShipsKilled(0);
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
