package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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

import java.util.ArrayList;
import java.util.List;

/**
 * Даний клас реалізовує графічний інтерфейс та функції екрану допомоги
 */
public class HelpScreen extends ScreenAdapter implements InputProcessor {

    private SeaBattleGame game;
    private BitmapFont font;
    private BitmapFont fontBig;
    private Stage stage;
    private SpriteBatch batch;
    private Sprite sprite;
    private ImageButton forward;
    private ImageButton back;
    private ImageButton menu;
    private int level;
    private int helpStage;
    private Label text;
    private Image illustration;
    private ArrayList<String> helpMessages = new ArrayList<>();
    private ArrayList<Image> helpImages = new ArrayList<>();
    private int bonusScore;

    public HelpScreen(SeaBattleGame game, int level, int bonusScore)
    {
        this.bonusScore = bonusScore;
        this.level = level;
        this.game = game;
        helpStage = 0;
        this.fillHelpList();

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

        /**
         * кнопка вперед
         */

        Texture forwardTexture = new Texture(Gdx.files.internal("forward.png"));
        TextureRegion forwardTextureRegion = new TextureRegion(forwardTexture);
        TextureRegionDrawable forwardTexRegionDrawable = new TextureRegionDrawable(forwardTextureRegion);
        forward = new ImageButton(forwardTexRegionDrawable);
        forward.setPosition(Gdx.graphics.getWidth()-forward.getWidth()-15,Gdx.graphics.getHeight()-forward.getHeight()-550);
        stage.addActor(forward);

        /**
         * кнопка назад
         */

        Texture backTexture = new Texture(Gdx.files.internal("backHelp.png"));
        TextureRegion backTextureRegion = new TextureRegion(backTexture);
        TextureRegionDrawable backTexRegionDrawable = new TextureRegionDrawable(backTextureRegion);
        back = new ImageButton(backTexRegionDrawable);
        back.setPosition(0,Gdx.graphics.getHeight()-back.getHeight()-550);
        stage.addActor(back);


        /**
         * текст
         */
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Zyana.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 40;
        parameter.color = Color.BLACK;
        parameter.borderWidth = 1;
        parameter.borderColor = Color.GRAY;
        font = generator.generateFont(parameter);
        Label.LabelStyle style = new Label.LabelStyle();
        style.font = font;
        text = new Label(helpMessages.get(0),style);
        text.setPosition(0,0);
        text.setSize(70,50);
        text.setWrap(true);
        Pixmap labelColor2 = new Pixmap((int) text.getWidth(),(int) text.getHeight(), Pixmap.Format.RGB888);
        labelColor2.setColor(Color.WHITE);
        labelColor2.fill();
        text.getStyle().background = new Image(new Texture(labelColor2)).getDrawable();


        FreeTypeFontGenerator generatorBig = new FreeTypeFontGenerator(Gdx.files.internal("ukr.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameterBig = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameterBig.size = 50;
        parameterBig.color = Color.WHITE;
        parameterBig.borderWidth = 3;
        parameterBig.borderColor = Color.BLACK;
        fontBig = generatorBig.generateFont(parameterBig);

        for(Image i:helpImages) {
            i.setPosition(Gdx.graphics.getWidth()/3-30,Gdx.graphics.getHeight()/3-200);
            if(i==helpImages.get(helpImages.size()-1)) i.setPosition(Gdx.graphics.getWidth()/3, Gdx.graphics.getHeight()/3-100);
            else if(i==helpImages.get(helpImages.size()-2)) i.setPosition(Gdx.graphics.getWidth()/3+30, Gdx.graphics.getHeight()/3-190);
            stage.addActor(i);
            i.setVisible(false);
        }
        helpImages.get(0).setVisible(true);

        sprite = new Sprite(new Texture(Gdx.files.internal("helpBackground.jpg")));

        sprite.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.input.setInputProcessor(this);

    }

    @Override
    public void show(){}

    /**
     * Малюємо форму допомоги
     */
    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);
        batch.begin();
        sprite.draw(batch);

        fontBig.draw(batch,"Help",Gdx.graphics.getWidth()/2 - 100,Gdx.graphics.getHeight()-20);
        font.draw(batch,helpMessages.get(helpStage),10,Gdx.graphics.getHeight()-150);
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
            game.setScreen(new MainMenu(game,level,bonusScore));
        }

        else if(hitActor==forward.getImage()) {
            if(game.soundState)  game.clicksound.play();
            if(this.helpStage<this.helpMessages.size()-1) {
                helpStage++;
                text.setText(helpMessages.get(helpStage));
                helpImages.get(helpStage-1).setVisible(false);
                helpImages.get(helpStage).setVisible(true);
            }

        }
        else if(hitActor==back.getImage()) {
            if(game.soundState) game.clicksound.play();
            if(this.helpStage>0) {
                helpStage--;
                text.setText(helpMessages.get(helpStage));

                helpImages.get(helpStage+1).setVisible(false);
                helpImages.get(helpStage).setVisible(true);

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

    /**
     * Заповнюєм інформацію про гру для форми допомоги
     */
    private void fillHelpList() {
        this.helpMessages = new ArrayList<>();
        helpMessages.add("Sea Battle is a strategy type guessing game where you play against the computer.\nYour goal is to destroy the fleet of warships of your opponent, while it tries to do\n the same. Whoever destroys the opponent's fleet first is the winner.");
        helpMessages.add("Game consists of three levels. Firstly, you need to locate your warships on the grid\n by dragging them. Their location will be concealed from your opponent.\n After the game starts, you take turns trying to find each other's warships.");
        helpMessages.add("On second and third levels additional bonuses can be bought. However, amount\n is limited. The better you performed on your previous level, the more bonuses you\n can buy. On last level random events will also start occuring in the game.");
        this.helpImages = new ArrayList<>();
        helpImages.add(new Image(new Texture(Gdx.files.internal("illustration1.png"))));
        helpImages.add(new Image(new Texture(Gdx.files.internal("illustration2.png"))));
        helpImages.add(new Image(new Texture(Gdx.files.internal("illustration3.png"))));

    }

}
