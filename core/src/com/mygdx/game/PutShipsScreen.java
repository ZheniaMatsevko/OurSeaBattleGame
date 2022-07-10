package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.*;
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
import com.badlogic.gdx.scenes.scene2d.ui.*;
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
    private BitmapFont errorFont;
    private ImageButton putAgainButton;
    private ImageButton playButton;
    private Skin skin;
    private PlayGround playGround;
    private int level;
    private Image radar;
    private Image bomb;
    private Label bonusAvailableLabel;
    private Label countBombsLabel;
    private Label countRadarsLabel;
    private BombInfoDialog bombInfoDialog;
    private RadarInfoDialog radarInfoDialog;
    private int numberOfBombs;
    private int numberOfRadars;
    private Image plusBomb;
    private Image minusBomb;
    private Image plusRadar;
    private Image minusRadar;
    private int bonusScore;
    private Label noMoreBonus;
    private Label bonusTooExpensive;
    private BitmapFont fontBig;
    private String message;

    public PutShipsScreen(SeaBattleGame game, int level, int bonusScore) {
        this.bonusScore = bonusScore;
        this.level = level;
        skin = new Skin(Gdx.files.internal("uiskin.json"));
        this.game = game;
        radar = new Image(new Texture(Gdx.files.internal("radar2.png")));
        radar.setPosition(Gdx.graphics.getWidth()/9*6-70, Gdx.graphics.getHeight()-radar.getHeight()-120);
        bomb = new Image(new Texture(Gdx.files.internal("bomb.png")));
        bomb.setPosition(radar.getX()+radar.getWidth()+100, radar.getY()+5);
        minusRadar = new Image(new Texture(Gdx.files.internal("minus.png")));
        minusRadar.setPosition(radar.getX()-5,radar.getY()-minusRadar.getHeight()-10);
        plusRadar = new Image(new Texture(Gdx.files.internal("plus.png")));
        plusRadar.setPosition(radar.getX()+minusRadar.getWidth()+60,radar.getY()-plusRadar.getHeight()-10);
        minusBomb = new Image(new Texture(Gdx.files.internal("minus.png")));
        minusBomb.setPosition(bomb.getX()-15,bomb.getY()-minusBomb.getHeight()-10);
        plusBomb = new Image(new Texture(Gdx.files.internal("plus.png")));
        plusBomb.setPosition(bomb.getX()+minusBomb.getWidth()+50,bomb.getY()-plusBomb.getHeight()-10);
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Zyana.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 40;
        parameter.color = Color.BLACK;
        parameter.borderWidth = 1;
        parameter.borderColor = Color.GRAY;
        font = generator.generateFont(parameter);
        FreeTypeFontGenerator generator7 = new FreeTypeFontGenerator(Gdx.files.internal("Zyana.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter7 = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter7.size = 33;
        parameter7.color = Color.RED;
        parameter7.borderWidth = 1;
        parameter7.borderColor = Color.BLACK;
        errorFont = generator7.generateFont(parameter7);
        FreeTypeFontGenerator generator1 = new FreeTypeFontGenerator(Gdx.files.internal("Zyana.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter1 = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter1.size = 45;
        parameter1.color = Color.BLACK;
        parameter1.borderWidth = 1;
        parameter1.borderColor = Color.GRAY;
        BitmapFont font1 = generator1.generateFont(parameter1);
        Label.LabelStyle style = new Label.LabelStyle();
        style.font = font1;
        message = "Put ships on the field";
        style.fontColor = Color.BLACK;
        bonusAvailableLabel = new Label("  Available on level 2",style);
        bonusAvailableLabel.setPosition(radar.getX()-10,radar.getY()+60);
        bonusAvailableLabel.setSize(430,50);
        Pixmap labelColor = new Pixmap((int) bonusAvailableLabel.getWidth(),(int) bonusAvailableLabel.getHeight(), Pixmap.Format.RGB888);
        labelColor.setColor(Color.GRAY);
        labelColor.fill();
        bonusAvailableLabel.getStyle().background = new Image(new Texture(labelColor)).getDrawable();

        Label.LabelStyle style2 = new Label.LabelStyle();
        style2.font = font;
        style2.fontColor = Color.BLACK;
        countBombsLabel = new Label(" 0",style2);
        countRadarsLabel = new Label(" 0",style2);
        countRadarsLabel.setPosition(minusRadar.getX()+minusRadar.getWidth()+13,plusRadar.getY()+8);
        countRadarsLabel.setSize(40,40);
        countBombsLabel.setPosition(minusBomb.getX()+minusBomb.getWidth()+13,plusBomb.getY()+8);
        countBombsLabel.setSize(40,40);
        Pixmap labelColor2 = new Pixmap((int) countBombsLabel.getWidth(),(int) countBombsLabel.getHeight(), Pixmap.Format.RGB888);
        labelColor2.setColor(Color.WHITE);
        labelColor2.fill();
        countBombsLabel.getStyle().background = new Image(new Texture(labelColor2)).getDrawable();
        Pixmap labelColor3 = new Pixmap((int) countRadarsLabel.getWidth(),(int) countRadarsLabel.getHeight(), Pixmap.Format.RGB888);
        labelColor3.setColor(Color.WHITE);
        labelColor3.fill();
        countRadarsLabel.getStyle().background = new Image(new Texture(labelColor3)).getDrawable();

        stage = new Stage(new ScreenViewport());
        playGround = new PlayGround(10,10, 96, Gdx.graphics.getHeight()-100);
        batch = new SpriteBatch();
        sprite = new Sprite(new Texture(Gdx.files.internal("back4.jpg")));
        sprite.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Texture myTexture = new Texture(Gdx.files.internal("again.png"));
        TextureRegion myTextureRegion = new TextureRegion(myTexture);
        TextureRegionDrawable myTexRegionDrawable = new TextureRegionDrawable(myTextureRegion);
        putAgainButton = new ImageButton(myTexRegionDrawable);
        putAgainButton.setPosition(Gdx.graphics.getWidth()/9*3+120,Gdx.graphics.getHeight()-putAgainButton.getHeight()-200);
        Texture myTexture1 = new Texture(Gdx.files.internal("start.png"));
        TextureRegion myTextureRegion1 = new TextureRegion(myTexture1);
        TextureRegionDrawable myTexRegionDrawable1 = new TextureRegionDrawable(myTextureRegion1);
        playButton = new ImageButton(myTexRegionDrawable1);
        putAgainButton.setName("Again");
        playButton.setPosition(Gdx.graphics.getWidth()/9*3+80,putAgainButton.getY()-putAgainButton.getHeight());
        stage.addActor(putAgainButton);
        stage.addActor(playGround);
        stage.addActor(playButton);
        stage.addActor(radar);
        stage.addActor(bomb);


        /**текст про недостатню кількість очок для придбання бонусів або досягнутого ліміта по бонусам**/
        FreeTypeFontGenerator generatorBig = new FreeTypeFontGenerator(Gdx.files.internal("ukr.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameterBig = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameterBig.size = 20;
        parameterBig.color = Color.WHITE;
        parameterBig.borderWidth = 3;
        parameterBig.borderColor = Color.BLACK;
        fontBig = generatorBig.generateFont(parameterBig);
        Label.LabelStyle styleBonus = new Label.LabelStyle();
        styleBonus.font = fontBig;
        styleBonus.fontColor = Color.RED;
        bonusTooExpensive = new Label("You don't have enough points!",styleBonus);
        bonusTooExpensive.setPosition(Gdx.graphics.getWidth()/3+410,Gdx.graphics.getHeight()/3+30);
        stage.addActor(bonusTooExpensive);

        noMoreBonus = new Label("You have already reached the limit\n for this bonus!",styleBonus);
        noMoreBonus.setPosition(Gdx.graphics.getWidth()/3+350,Gdx.graphics.getHeight()/3+30);
        noMoreBonus.setVisible(false);
        bonusTooExpensive.setVisible(false);
        stage.addActor(noMoreBonus);



        if(level==1){
            radar.setColor(Color.BLACK);
            bomb.setColor(Color.BLACK);
            stage.addActor(bonusAvailableLabel);
        }else{
            stage.addActor(plusBomb);
            stage.addActor(plusRadar);
            stage.addActor(minusBomb);
            stage.addActor(minusRadar);
            stage.addActor(countBombsLabel);
            stage.addActor(countRadarsLabel);
        }
         bombInfoDialog = new BombInfoDialog("Bomb information",skin);
        bombInfoDialog.setVisible(false);
        stage.addActor(bombInfoDialog);
        radarInfoDialog = new RadarInfoDialog("Radar information",skin);
        radarInfoDialog.setVisible(false);
        stage.addActor(radarInfoDialog);
        Gdx.input.setInputProcessor(this);
    }
    public static class BombInfoDialog extends Dialog{

        public BombInfoDialog(String title, Skin skin) {
            super(title, skin);
        }

        public BombInfoDialog(String title, Skin skin, String windowStyleName) {
            super(title, skin, windowStyleName);
        }
        {
            text("It`s a cool bomb with which you can kill\nseveral cells at once!\nCosts: 2 points");
            button("OK");
        }

        @Override
        protected void result(Object object) {
            this.setVisible(false);
        }

        public BombInfoDialog(String title, WindowStyle windowStyle) {
            super(title, windowStyle);
        }
    }
    public static class RadarInfoDialog extends Dialog{

        public RadarInfoDialog(String title, Skin skin) {
            super(title, skin);
        }

        public RadarInfoDialog(String title, Skin skin, String windowStyleName) {
            super(title, skin, windowStyleName);
        }
        {
            text("It`s a cool radar with which you can check\nseveral cells at once for ships!\nCosts: 1 point");
            button("OK");
        }

        @Override
        protected void result(Object object) {
            this.setVisible(false);
        }

        public RadarInfoDialog(String title, WindowStyle windowStyle) {
            super(title, windowStyle);
        }
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);
        if(playGround.isBoatChanged()==0 && playGround.getChangedBoat()==null){
            playGround.putShipsOnItsPlaces();
        }
        batch.begin();
        sprite.draw(batch);
        font.draw(batch,"Level "+level,235,Gdx.graphics.getHeight()-50);
        if(playGround.getMessage()!=null)
            errorFont.draw(batch,playGround.getMessage(),80,150);
        else
            font.draw(batch,"Put ships on the field!",110,150);
        font.draw(batch,"Bonuses (your score: " + bonusScore + ")",Gdx.graphics.getWidth()-565,Gdx.graphics.getHeight()-50);
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
    public void setText(String mess){
        this.message=mess;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        removeWarnings();
        Vector2 coord = stage.screenToStageCoordinates(new Vector2((float)screenX,(float) screenY));
        Actor hitActor = playGround.getStage().hit(coord.x,coord.y,true);
        Actor hitActor2 = stage.hit(coord.x,coord.y,true);
        if(hitActor2!=null && hitActor2.getClass()== Label.class && ((Label)hitActor2).getText().length==2){
            System.out.println(hitActor2);
            bombInfoDialog.setVisible(false);
            radarInfoDialog.setVisible(false);
        }
        else if(hitActor2==bomb && level>1){
            bombInfoDialog.setVisible(true);
            bombInfoDialog.show(stage);
        }
        else if(hitActor2==radar && level>1){
            radarInfoDialog.setVisible(true);
            radarInfoDialog.show(stage);
        }
        else if(hitActor2==plusBomb){
            if(bonusScore>=2 && numberOfBombs<2) {
                numberOfBombs++;
                bonusScore -= 2;
                System.out.println(bonusScore);
                bonusTooExpensive.setVisible(false);
            }
            else if (numberOfBombs<2){
                removeWarnings();
                bonusTooExpensive.setVisible(true);
            }
            else {
                removeWarnings();
                noMoreBonus.setVisible(true);
            }
            countBombsLabel.setText(" " + numberOfBombs);
        }else if(hitActor2==minusBomb && numberOfBombs>0){
            numberOfBombs--;
            bonusScore+=2;
            System.out.println(bonusScore);
            countBombsLabel.setText(" " + numberOfBombs);
        }else if(hitActor2==plusRadar){
            if (bonusScore >= 1 && numberOfRadars<3) {
            numberOfRadars++;
            bonusScore--;
            System.out.println(bonusScore);
            bonusTooExpensive.setVisible(false);
            }
            else if (numberOfRadars<3){
                removeWarnings();
                bonusTooExpensive.setVisible(true);
            }
            else {
                removeWarnings();
                noMoreBonus.setVisible(true);
            }
            countRadarsLabel.setText(" " + numberOfRadars);
        }else if(hitActor2==minusRadar && numberOfRadars>0){
            numberOfRadars--;
            bonusScore++;
            System.out.println(bonusScore);
            countRadarsLabel.setText(" " + numberOfRadars);
        }
        else if(hitActor2==playButton.getImage()){
            System.out.println("Play button");
            game.setScreen(new MainGameScreen(game,playGround,level,numberOfBombs,numberOfRadars,bonusScore));
        }else if(hitActor2==putAgainButton.getImage()){
            System.out.println("AGAIN button");
            playGround.cleanField();
            playGround.putRandomBoats();
        }
        else if(hitActor!=null){
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
        }
       // if(hitActor!=plusBomb&&hitActor!=plusRadar) {
         //   removeWarnings();
        //}
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
        if(hitActor==null && playGround.isBoatChanged()==1) {
            playGround.putShipsOnItsPlaces();
            playGround.setIsBoatChanged(0);
        }
        else if(hitActor!=null){
            if(hitActor.getClass()==Cell.class){
                playGround.setCellDragged(true);
                if(playGround.isBoatChanged()==1){
                    playGround.putShipsOnItsPlaces();
                    playGround.setIsBoatChanged(0);
                }
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

    private void removeWarnings() {
        bonusTooExpensive.setVisible(false);
        noMoreBonus.setVisible(false);
    }
}
