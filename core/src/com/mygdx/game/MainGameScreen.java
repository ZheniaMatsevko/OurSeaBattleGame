package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class MainGameScreen extends ScreenAdapter {
    ComputerGround computerGround;
    PlayGround userGround;
    SeaBattleGame game;
    BitmapFont font;
    Stage stage;
    SpriteBatch batch;
    Sprite sprite;
    public MainGameScreen(SeaBattleGame game) {
        this.game = game;
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Zyana.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 30;
        parameter.color = Color.BLACK;
        parameter.borderWidth = 1;
        parameter.borderColor = Color.GRAY;
        font = generator.generateFont(parameter);
        stage = new Stage(new ScreenViewport());
        computerGround = new ComputerGround(10,10);
        userGround = new PlayGround(10,10);
        batch = new SpriteBatch();
        sprite = new Sprite(new Texture(Gdx.files.internal("putships.jpg")));
        sprite.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage.addActor(userGround);
        Gdx.input.setInputProcessor(stage);
    }
    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);
        batch.begin();
        String text = "SEA BATTLE";
        sprite.draw(batch);
        font.draw(batch,"SEA BATTLE",Gdx.graphics.getWidth()/4,Gdx.graphics.getHeight()/4*3);
        batch.end();
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }
}
