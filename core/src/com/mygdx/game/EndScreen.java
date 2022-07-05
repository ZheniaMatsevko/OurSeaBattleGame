package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class EndScreen extends ScreenAdapter implements InputProcessor {
    private SeaBattleGame game;
    private BitmapFont font;
    private Stage stage;
    private SpriteBatch batch;
    private Sprite sprite;
    private int whoWin;
    private Image victory;
    private Image gameOver;
    private Image restart;
    private Image menu;
    private Image continueImage;
    private int score;
    private int level;
    public EndScreen(SeaBattleGame game,int whoWin, int score, int level){
        this.level = level;
        this.game = game;
        this.score = score;
        this.whoWin = whoWin;
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Zyana.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 50;
        parameter.color = Color.BLACK;
        parameter.borderWidth = 1;
        parameter.borderColor = Color.GRAY;
        font = generator.generateFont(parameter);
        stage = new Stage(new ScreenViewport());
        batch = new SpriteBatch();
        gameOver = new Image(new Texture(Gdx.files.internal("lose.png")));
        gameOver.setPosition(Gdx.graphics.getWidth()/9*5+50,Gdx.graphics.getHeight()-350);
        victory = new Image(new Texture(Gdx.files.internal("win0.png")));
        victory.setPosition(Gdx.graphics.getWidth()/9*6+50,Gdx.graphics.getHeight()-350);
        restart = new Image(new Texture(Gdx.files.internal("restart.png")));
        restart.setPosition(Gdx.graphics.getWidth()/9*5+130,Gdx.graphics.getHeight()-535);
        continueImage = new Image(new Texture(Gdx.files.internal("continue.png")));
        continueImage.setPosition(Gdx.graphics.getWidth()/9*6,Gdx.graphics.getHeight()-535);
        menu = new Image(new Texture(Gdx.files.internal("menu.png")));
        menu.setPosition(20,Gdx.graphics.getHeight()-menu.getHeight()-20);
        stage.addActor(menu);
        if(whoWin==1){
            this.level++;
            sprite = new Sprite(new Texture(Gdx.files.internal("win8.jpg")));
            stage.addActor(victory);
            stage.addActor(continueImage);
        }else{
            sprite = new Sprite(new Texture(Gdx.files.internal("loseBack.jpg")));
            stage.addActor(gameOver);
            stage.addActor(restart);
        }
        sprite.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.input.setInputProcessor(this);
    }
    @Override
    public void show(){

    }
    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);
        batch.begin();
        sprite.draw(batch);
        if(whoWin==-1)
            font.draw(batch,"Your score: " + score + "/10 boats",gameOver.getX()-30, gameOver.getY()-30);
        else
            font.draw(batch,"Your score: " + score + "/10 boats",victory.getX()-100, victory.getY()-30);
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
        System.out.println("Hjhj");
        if(whoWin==1){
            if(hitActor==continueImage){
                System.out.println("Hit " + hitActor.getClass());
                game.setScreen(new PutShipsScreen(game,level));
            }
        }else{
            if(hitActor==restart){
                System.out.println("Hit " + hitActor.getClass());
                game.setScreen(new PutShipsScreen(game,level));
            }
        }
        if(hitActor==menu){
            System.out.println("Hit " + hitActor.getClass());
            game.setScreen(new MainMenu(game,level));
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
