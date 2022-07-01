package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class PutShipsScreen extends ScreenAdapter implements InputProcessor {
    SeaBattleGame game;
    private SpriteBatch batch;
    private Sprite sprite;
    private Stage stage;
    private BitmapFont font;
    private ImageButton putAgainButton;
    private ImageButton playButton;
    private Skin skin;
    private PlayGround playGround;

    public PutShipsScreen(SeaBattleGame game) {
        skin = new Skin(Gdx.files.internal("uiskin.json"));
        this.game = game;
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Zyana.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 40;
        parameter.color = Color.BLACK;
        parameter.borderWidth = 1;
        parameter.borderColor = Color.GRAY;
        font = generator.generateFont(parameter);
        stage = new Stage(new ScreenViewport());
        playGround = new PlayGround(10,10);
        batch = new SpriteBatch();
        sprite = new Sprite(new Texture(Gdx.files.internal("putships.jpg")));
        sprite.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Texture myTexture = new Texture(Gdx.files.internal("again.png"));
        TextureRegion myTextureRegion = new TextureRegion(myTexture);
        TextureRegionDrawable myTexRegionDrawable = new TextureRegionDrawable(myTextureRegion);
        putAgainButton = new ImageButton(myTexRegionDrawable);
        putAgainButton.setPosition(Gdx.graphics.getWidth()/3*2+30,Gdx.graphics.getHeight()-putAgainButton.getHeight()-100);
        Texture myTexture1 = new Texture(Gdx.files.internal("start.png"));
        TextureRegion myTextureRegion1 = new TextureRegion(myTexture1);
        TextureRegionDrawable myTexRegionDrawable1 = new TextureRegionDrawable(myTextureRegion1);
        playButton = new ImageButton(myTexRegionDrawable1);
        playButton.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("Button");
                return true;
            }
        });
        putAgainButton.setName("Again");
        playButton.setPosition(Gdx.graphics.getWidth()/3*2,putAgainButton.getY()-putAgainButton.getHeight()-20);
        stage.addActor(putAgainButton);
        stage.addActor(playGround);
        stage.addActor(playButton);
        Gdx.input.setInputProcessor(this);

    }

    @Override
    public void render(float delta) {
        String text = "Put ships on the field";
        ScreenUtils.clear(0, 0, 0, 1);
        batch.begin();
        sprite.draw(batch);
        font.draw(batch,text,Gdx.graphics.getWidth()/4+25,90);
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
        Actor hitActor = playGround.getStage().hit(coord.x,coord.y,true);
        Actor hitActor2 = stage.hit(coord.x,coord.y,true);
        if(hitActor2==playButton.getImage()){
            System.out.println("Play button");
        }else if(hitActor2==putAgainButton.getImage()){
            System.out.println("AGAIN button");
            playGround.cleanField();
            playGround.putRandomBoats();
        }
        if(hitActor==null)
            System.out.println("Stage actor not found");
        else {
            System.out.println("yes" + hitActor.getName() + " " + hitActor.getX()+" " + hitActor.getY() + " class: " + hitActor.getClass());
            if(hitActor.getClass()==Boat.class){
                playGround.setPreviousY(hitActor.getY());
                playGround.setPreviousX(hitActor.getX());
                playGround.setCellDragged(false);
                if(((Boat) hitActor).getSize()>1){
                    playGround.setChangedBoat((Boat)hitActor);
                    playGround.setIsBoatChanged(4);
                }
            }
            if(hitActor.getClass()==Cell.class){
                playGround.setIsBoatChanged(0);
            }
            //Cell ace = (Cell) playGround.getGroup().findActor(hitActor.getName());
            //ace.changeColor();
        }
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if(playGround.isBoatChanged()==1)
            playGround.setIsBoatChanged(2);
        if(playGround.isBoatChanged()==4)
            playGround.Rotate();
        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        Vector2 coord = stage.screenToStageCoordinates(new Vector2((float)screenX,(float) screenY));
        Actor hitActor = playGround.getStage().hit(coord.x,coord.y,true);
        if(hitActor==null)
            System.out.println("Touch dragged actor not found");
        else {
            if(hitActor.getClass()==Cell.class){
                playGround.setCellDragged(true);
            }
            if(!playGround.isCellDragged() && hitActor.getClass()==Boat.class){

                playGround.setIsBoatChanged(1);
                playGround.setChangedBoat((Boat)hitActor);
                hitActor.setZIndex(200);
                if(((Boat) hitActor).getDirection()==0)
                    hitActor.setPosition(screenX-hitActor.getWidth()/2,Gdx.graphics.getHeight()-screenY-hitActor.getHeight()/2);
                else {
                    float loc = 0.5F;
                    for(int i=2;i<((Boat) hitActor).getSize();i++){
                        loc+=0.5;
                    }
                    hitActor.setPosition(screenX + hitActor.getWidth() / 2 - playGround.getCellWidth()*loc, Gdx.graphics.getHeight() - screenY - hitActor.getHeight() / 2- playGround.getCellHeight()*loc);
                }
            }
        }
        return true;
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
