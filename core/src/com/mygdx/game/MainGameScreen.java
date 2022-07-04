package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
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
    private BitmapFont fontNumbers;
    private Stage stage;
    private SpriteBatch batch;
    private Sprite sprite;
    private int whoIsNext;
    private int score;
    private Label turnLabel;
    private Label messageLabel;

    public MainGameScreen(SeaBattleGame game, PlayGround ground) {
        whoIsNext=0;
        this.game = game;
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("ukr.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 30;
        parameter.color = Color.BLACK;
        parameter.borderWidth = 3;
        parameter.borderColor = Color.GRAY;
        font = generator.generateFont(parameter);

        FreeTypeFontGenerator generator1 = new FreeTypeFontGenerator(Gdx.files.internal("Zyana.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter1 = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter1.size = 35;
        parameter1.color = Color.WHITE;
        parameter1.borderWidth = 1;
        parameter1.borderColor = Color.GRAY;
        BitmapFont font1 = generator1.generateFont(parameter1);
        Label.LabelStyle style = new Label.LabelStyle();
        style.font = font1;
        style.fontColor = Color.WHITE;
        turnLabel = new Label("     Your turn",style);
        turnLabel.setPosition(70,100);
        turnLabel.setSize(250,50);
        Pixmap labelColor = new Pixmap((int) turnLabel.getWidth(),(int) turnLabel.getHeight(), Pixmap.Format.RGB888);
        labelColor.setColor(Color.BLACK);
        labelColor.fill();
        turnLabel.getStyle().background = new Image(new Texture(labelColor)).getDrawable();
        FreeTypeFontGenerator generator2 = new FreeTypeFontGenerator(Gdx.files.internal("font2.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter2 = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter2.size = 25;
        parameter2.color = Color.BLACK;
        parameter2.borderWidth = 1;
        parameter2.borderColor = Color.GRAY;
        BitmapFont font2 = generator2.generateFont(parameter2);
        Label.LabelStyle style2 = new Label.LabelStyle();
        style2.font = font2;
        messageLabel = new Label("     Make your first step!",style2);
        messageLabel.setPosition(0,0);
        messageLabel.setSize(Gdx.graphics.getWidth(),40);
        Pixmap labelColor2 = new Pixmap((int) messageLabel.getWidth(),(int) messageLabel.getHeight(), Pixmap.Format.RGB888);
        labelColor2.setColor(Color.WHITE);
        labelColor2.fill();
        messageLabel.getStyle().background = new Image(new Texture(labelColor2)).getDrawable();
        stage = new Stage(new ScreenViewport());
        computerGround = new ComputerGround(10,10, Gdx.graphics.getWidth()/3 + 100,Gdx.graphics.getHeight()-100, ground);
        userGround = ground;
        ground.setNewPositions(60,Gdx.graphics.getHeight()-100);
        batch = new SpriteBatch();
        sprite = new Sprite(new Texture(Gdx.files.internal("back5.jpg")));
        FreeTypeFontGenerator generator4 = new FreeTypeFontGenerator(Gdx.files.internal("Zyana.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter4 = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter4.size = (int)userGround.getCellHeight()-2;
        parameter4.color = Color.BLACK;
        parameter4.borderWidth = 2;
        parameter4.borderColor = Color.GRAY;
        fontNumbers = generator4.generateFont(parameter4);
        sprite.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage.addActor(userGround);
        stage.addActor(computerGround);
        stage.addActor(turnLabel);
        stage.addActor(messageLabel);
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void show(){

    }
    public int checkForWin(){
        if(userGround.getScore()== userGround.getNumberOfCellsInRow()){
            return -1;
        }
        if(computerGround.getGround().getScore()== userGround.getNumberOfCellsInRow()){
            return 1;
        }
        return 0;
    }
    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);
        if(checkForWin()!=0){
            game.setScreen(new EndScreen(game,checkForWin(),computerGround.getGround().getScore()));
        }
        float delay = 2;
        if(whoIsNext==1){
            whoIsNext=3;
            turnLabel.setText(" Computer turn");
            Timer.schedule(new Timer.Task(){
                @Override
                public void run() {
                    computerGround.shoot(messageLabel);
                    if(computerGround.isStrike())
                        whoIsNext=1;
                    else {
                        turnLabel.setText("     Your turn");
                        whoIsNext=0;
                    }
                }
            }, delay);
        }
        batch.begin();
        sprite.draw(batch);
        font.draw(batch,"Your field",userGround.getCell(0,2).getX()+10,Gdx.graphics.getHeight()-20);
        font.draw(batch,"Computer field",computerGround.getGround().getCell(0,2).getX()-25,Gdx.graphics.getHeight()-20);
        String letters = "ABCDEFGHIJ";
        fontNumbers.draw(batch,String.valueOf(letters.charAt(0)),userGround.getCell(0,0).getX()+10,Gdx.graphics.getHeight()-67);
        fontNumbers.draw(batch,String.valueOf(userGround.getNumberOfCellsInRow()),20,userGround.getCell(9,0).getY()+userGround.getCellHeight()-6);
        fontNumbers.draw(batch,String.valueOf(letters.charAt(0)),computerGround.getGround().getCell(0,0).getX()+10,Gdx.graphics.getHeight()-67);
        fontNumbers.draw(batch,String.valueOf(userGround.getNumberOfCellsInRow()),computerGround.getGround().getCell(0,0).getX()-38,computerGround.getGround().getCell(9,0).getY()+userGround.getCellHeight()-6);

        for(int i=1;i<userGround.getNumberOfCellsInRow();i++){
            fontNumbers.draw(batch,String.valueOf(userGround.getNumberOfCellsInRow()-i),30,userGround.getCell(userGround.getNumberOfCellsInRow()-i-1,0).getY()+userGround.getCellHeight()-6);
            fontNumbers.draw(batch,String.valueOf(letters.charAt(i)),userGround.getCell(0,i).getX()+15,Gdx.graphics.getHeight()-67);
            fontNumbers.draw(batch,String.valueOf(letters.charAt(i)),computerGround.getGround().getCell(0,i).getX()+15,Gdx.graphics.getHeight()-67);
            fontNumbers.draw(batch,String.valueOf(userGround.getNumberOfCellsInRow()-i),computerGround.getGround().getCell(0,0).getX()-28,computerGround.getGround().getCell(userGround.getNumberOfCellsInRow()-i-1,0).getY()+userGround.getCellHeight()-6);

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
                            if(computerGround.getGround().killCell((Cell)hitActor2)){
                                messageLabel.setText("      User shot at " + computerGround.getGround().getCellName((Cell)hitActor2) + " and killed the ship!");
                            }else{
                                messageLabel.setText("      User shot at " + computerGround.getGround().getCellName((Cell)hitActor2) + " and damaged the ship!");
                            }
                            ((Cell) hitActor2).changeColor(Color.GREEN);
                        }else{
                            ((Cell) hitActor2).changeColor(Color.GRAY);
                            ((Cell) hitActor2).setShot(true);
                            messageLabel.setText("      User shot at " + computerGround.getGround().getCellName((Cell)hitActor2) + " and missed.");
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
                     computerGround.getGround().getPaintedCell().changeColor(Color.WHITE);
                }
                computerGround.getGround().setPaintedCell(null);
            }
        }
        else {
            if(whoIsNext==0){
                if(hitActor2.getClass()==Cell.class){
                    if(computerGround.getGround().getPaintedCell()==null && !((Cell) hitActor2).isShot()) {
                        ((Cell) hitActor2).changeColor(Color.GREEN);
                        computerGround.getGround().setPaintedCell((Cell)hitActor2);
                    }else if(!((Cell) hitActor2).equals(computerGround.getGround().getPaintedCell())&& !((Cell) hitActor2).isShot()){
                        if(!computerGround.getGround().getPaintedCell().isShot()) {
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
