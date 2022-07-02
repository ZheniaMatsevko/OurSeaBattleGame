package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class MainGameScreen extends ScreenAdapter implements InputProcessor {
    private ComputerGround computerGround;
    private PlayGround userGround;
    private SeaBattleGame game;
    private BitmapFont font;
    private Stage stage;
    private SpriteBatch batch;
    private Sprite sprite;
    private int whoIsNext;

    public MainGameScreen(SeaBattleGame game, PlayGround ground) {
        whoIsNext=0;
        this.game = game;
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Zyana.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 30;
        parameter.color = Color.BLACK;
        parameter.borderWidth = 1;
        parameter.borderColor = Color.GRAY;
        font = generator.generateFont(parameter);

        stage = new Stage(new ScreenViewport());
        computerGround = new ComputerGround(10,10, Gdx.graphics.getWidth()/3 + 50,Gdx.graphics.getHeight()-50, ground);
        userGround = ground;
        ground.setNewPositions(10,Gdx.graphics.getHeight()-50);
        batch = new SpriteBatch();
        sprite = new Sprite(new Texture(Gdx.files.internal("putships.jpg")));
        sprite.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage.addActor(userGround);
        stage.addActor(computerGround);
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void show(){

    }
    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);
        if(whoIsNext==1){
            computerGround.shoot();
            if(!computerGround.isStrike())
                whoIsNext=0;
        }
        batch.begin();
        sprite.draw(batch);
        font.draw(batch,"Your field",10,Gdx.graphics.getHeight()-20);
        font.draw(batch,"Computer field",Gdx.graphics.getWidth()/3 + 50,Gdx.graphics.getHeight()-20);
        if(whoIsNext==0){
            font.draw(batch,"Your turn",20,40);
        }else{
            font.draw(batch,"Computer turn",20,40);
        }
        batch.end();
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }
    @Override
    public void hide(){
        Gdx.input.setInputProcessor(null);
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
        Actor hitActor2 = computerGround.getGround().getStage().hit(coord.x,coord.y,true);
        if(hitActor2==null)
            System.out.println("Comp actor not found");
        else {
            if(whoIsNext==0){
                System.out.println("yes" + hitActor2.getName() + " " + hitActor2.getX()+" " + hitActor2.getY() + " class: " + hitActor2.getClass());
                if(hitActor2.getClass()==Cell.class){
                    if(!((Cell) hitActor2).isShot()){
                        if(computerGround.getGround().checkShotCell((Cell)hitActor2)){
                            computerGround.getGround().killCell((Cell)hitActor2);
                        }else{
                            ((Cell) hitActor2).changeColor(Color.GRAY);
                            ((Cell) hitActor2).setShot(true);
                            whoIsNext=1;
                        }
                    }
                }

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
        Vector2 coord = stage.screenToStageCoordinates(new Vector2((float)screenX,(float) screenY));
        Actor hitActor2 = computerGround.getGround().getStage().hit(coord.x,coord.y,true);
        if(hitActor2==null) {
            if(computerGround.getGround().getPaintedCell()!=null && !computerGround.getGround().getPaintedCell().isShot()){
                if(computerGround.getGround().getPaintedCell().getColor()!=Color.GRAY) {
                    System.out.println(computerGround.getGround().getPaintedCell().getName() + " " + computerGround.getGround().getPaintedCell().getColor());
                    computerGround.getGround().getPaintedCell().changeColor(Color.WHITE);
                }
                computerGround.getGround().setPaintedCell(null);
            }
            System.out.println("Comp actor not found");
        }
        else {
            if(whoIsNext==0){
                System.out.println("yes" + hitActor2.getName() + " " + hitActor2.getX()+" " + hitActor2.getY() + " class: " + hitActor2.getClass());
                if(hitActor2.getClass()==Cell.class){
                    if(computerGround.getGround().getPaintedCell()==null && !((Cell) hitActor2).isShot()) {
                        ((Cell) hitActor2).changeColor(Color.GREEN);
                        computerGround.getGround().setPaintedCell((Cell)hitActor2);
                    }else if(!((Cell) hitActor2).equals(computerGround.getGround().getPaintedCell())&& !((Cell) hitActor2).isShot()){
                        if(!computerGround.getGround().getPaintedCell().isShot()) {
                            System.out.println(computerGround.getGround().getPaintedCell().getName() + " " + computerGround.getGround().getPaintedCell().getColor());
                            computerGround.getGround().getPaintedCell().changeColor(Color.WHITE);
                        }
                        ((Cell) hitActor2).changeColor(Color.GREEN);
                        computerGround.getGround().setPaintedCell((Cell)hitActor2);
                    }
                }
            }
        }
        return true;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }
}
