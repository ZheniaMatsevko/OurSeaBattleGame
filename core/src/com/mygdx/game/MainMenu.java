package com.mygdx.game;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class MainMenu extends ScreenAdapter {
    final SeaBattleGame game;
    private SpriteBatch batch;
    private Sprite sprite;

    private Stage stage;
    private Skin skin;

    private Table table;
    private TextButton singleModeButton;
    private TextButton twoModeButton;
    private ImageButton settingsButton;
    private ImageButton helpButton;
    private BitmapFont font;

    public MainMenu(final SeaBattleGame game) {
        this.game = game;
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Zyana.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 85;
        parameter.borderWidth = 1;
        parameter.borderColor = Color.GRAY;
        font = generator.generateFont(parameter);

        skin = new Skin(Gdx.files.internal("uiskin.json"));
        stage = new Stage(new ScreenViewport());
        table = new Table();
        table.setWidth(stage.getWidth());
        table.align(Align.center|Align.top);
        table.setPosition(0,Gdx.graphics.getHeight()-150);

        singleModeButton = new TextButton("Single mode",skin);
        singleModeButton.addListener( new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                hide();
                game.setPutScreen();
            }
        });
        twoModeButton = new TextButton("Two mode", skin);
        twoModeButton.getLabel().setFontScale(2,2);
        singleModeButton.getLabel().setFontScale(2,2);
        Texture myTexture = new Texture(Gdx.files.internal("Settings.png"));
        TextureRegion myTextureRegion = new TextureRegion(myTexture);
        TextureRegionDrawable myTexRegionDrawable = new TextureRegionDrawable(myTextureRegion);
        settingsButton = new ImageButton(myTexRegionDrawable);
        settingsButton.setPosition(10,Gdx.graphics.getHeight()-settingsButton.getHeight()-5);
        Texture myTexture2 = new Texture(Gdx.files.internal("q1.png"));
        TextureRegion myTextureRegion2 = new TextureRegion(myTexture2);
        TextureRegionDrawable myTexRegionDrawable2 = new TextureRegionDrawable(myTextureRegion2);
        helpButton = new ImageButton(myTexRegionDrawable2);
        helpButton.setPosition(settingsButton.getWidth()+20,settingsButton.getY());
        table.padTop(100);
        table.add(singleModeButton).padBottom(15);
        table.row();
        table.add(twoModeButton).padBottom(15);
        table.row();
        stage.addActor(settingsButton);
        stage.addActor(helpButton);
        stage.addActor(table);



        batch = new SpriteBatch();
        sprite = new Sprite(new Texture(Gdx.files.internal("sea.png")));
        sprite.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        Gdx.input.setInputProcessor(stage);
    }
    public Stage getStage(){
        return this.stage;
    }

    @Override
    public void show(){
        /*Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean keyDown(int keyCode) {
                if (keyCode == Input.Keys.SPACE) {
                    //game.setScreen(new GameScreen(game));
                }
                return true;
            }
        });*/
    }

    @Override
    public void render(float delta) {

        ScreenUtils.clear(0, 0, 0, 1);
        batch.begin();
        String text = "SEA BATTLE";
        sprite.draw(batch);
        font.draw(batch,"SEA BATTLE",Gdx.graphics.getWidth()/3+20,Gdx.graphics.getHeight()/4*3);
        batch.end();
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    @Override
    public void hide(){
        Gdx.input.setInputProcessor(null);
    }

}
