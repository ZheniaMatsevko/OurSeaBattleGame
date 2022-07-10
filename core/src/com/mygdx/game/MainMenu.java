package com.mygdx.game;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class MainMenu extends ScreenAdapter implements InputProcessor{
    SeaBattleGame game;
    private SpriteBatch batch;
    private Sprite sprite;
    private Stage stage;
    private int level;
    private ImageButton startButton;
    private ImageButton settingsButton;
    private ImageButton helpButton;
    private BitmapFont font;
    private int bonusScore;

    public MainMenu(final SeaBattleGame game, int level, int bonusScore) {
        this.level =level;
        this.game = game;
        this.bonusScore = bonusScore;
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Zyana.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 105;
        parameter.borderWidth = 2;
        parameter.borderColor = Color.GRAY;
        font = generator.generateFont(parameter);
        stage = new Stage(new ScreenViewport());
        Texture myTexture3 = new Texture(Gdx.files.internal("play3.png"));
        TextureRegion myTextureRegion3 = new TextureRegion(myTexture3);
        TextureRegionDrawable myTexRegionDrawable3 = new TextureRegionDrawable(myTextureRegion3);
        startButton = new ImageButton(myTexRegionDrawable3);
        startButton.setPosition(Gdx.graphics.getWidth()/3-80,Gdx.graphics.getHeight()/3-50);
        Texture myTexture2 = new Texture(Gdx.files.internal("help6.png"));
        TextureRegion myTextureRegion2 = new TextureRegion(myTexture2);
        TextureRegionDrawable myTexRegionDrawable2 = new TextureRegionDrawable(myTextureRegion2);
        helpButton = new ImageButton(myTexRegionDrawable2);
        helpButton.setPosition(startButton.getWidth()+startButton.getX()+50,startButton.getY());
        Texture myTexture = new Texture(Gdx.files.internal("set3.png"));
        TextureRegion myTextureRegion = new TextureRegion(myTexture);
        TextureRegionDrawable myTexRegionDrawable = new TextureRegionDrawable(myTextureRegion);
        settingsButton = new ImageButton(myTexRegionDrawable);
        settingsButton.setPosition(helpButton.getX()+helpButton.getWidth()+50,startButton.getY()+10);
        stage.addActor(settingsButton);
        stage.addActor(helpButton);
        stage.addActor(startButton);
        batch = new SpriteBatch();
        sprite = new Sprite(new Texture(Gdx.files.internal("sea.png")));
        sprite.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.input.setInputProcessor(this);
    }
    public Stage getStage(){
        return this.stage;
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);
        batch.begin();
        sprite.draw(batch);
        font.draw(batch,"SEA BATTLE",Gdx.graphics.getWidth()/3+20,Gdx.graphics.getHeight()/4*3);
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
        Vector2 coord = stage.screenToStageCoordinates(new Vector2((float)screenX,(float) screenY));
        Actor hitActor = stage.hit(coord.x,coord.y,true);
        if(hitActor==startButton.getImage()){
            game.clicksound.play();
            System.out.println("Hit " + hitActor.getClass());
            game.setScreen(new PutShipsScreen(game,level,bonusScore));
        }else if(hitActor==helpButton.getImage()){
            game.clicksound.play();
            System.out.println("Hit " + hitActor.getClass());
            game.setScreen(new HelpScreen(game,level,bonusScore));
        }
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return true;
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
