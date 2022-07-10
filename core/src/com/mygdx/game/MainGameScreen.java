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
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.LinkedList;
import java.util.Random;

public class MainGameScreen extends ScreenAdapter implements InputProcessor {
    private ComputerGround computerGround;
    private PlayGround userGround;
    private SeaBattleGame game;
    private BitmapFont font;
    private BitmapFont bigFont;
    private BitmapFont fontNumbers;
    private Stage stage;
    private SpriteBatch batch;
    private Sprite sprite;
    private int whoIsNext;
    private Label turnLabel;
    private Label messageLabel;
    private Image pause;
    private int level;
    private int numberOfBombs;
    private int numberOfRadars;
    private Skin skin;
    private PauseDialog pauseDialog;
    private Image radar;
    private Image bomb;
    private int bonusChosen;
    private int bonusScore;
    private int randomAction = 0;
    private Random random = new Random();
    private Image expl = new Image();
    private Image wave;
    private int waveStage = 0;
     int waveX = 600;
     int waveY = 550;
    final int stepY = 5;
    private int shouldWave;
    private float waveDelay = 1;
    private boolean whoWave;


    public MainGameScreen(SeaBattleGame game, PlayGround ground, int level, int numberOfBombs, int numberOfRadars, int bonusScore) {

        this.bonusScore = bonusScore;
        skin = new Skin(Gdx.files.internal("star-soldier-ui.json"));
        this.numberOfBombs = numberOfBombs;
        this.numberOfRadars = numberOfRadars;
        this.level = level;
        whoIsNext = 0;
        this.game = game;
        radar = new Image(new Texture(Gdx.files.internal("radar2.png")));
        radar.setPosition(Gdx.graphics.getWidth() - radar.getWidth() - 60, Gdx.graphics.getHeight() - radar.getHeight() - 120);
        bomb = new Image(new Texture(Gdx.files.internal("bomb.png")));
        bomb.setPosition(radar.getX(), radar.getY() - bomb.getHeight() - 20);
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("ukr.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 30;
        parameter.color = Color.BLACK;
        parameter.borderWidth = 3;
        parameter.borderColor = Color.GRAY;
        font = generator.generateFont(parameter);
        FreeTypeFontGenerator generator5 = new FreeTypeFontGenerator(Gdx.files.internal("Zyana.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter5 = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter5.size = 50;
        parameter5.color = Color.BLACK;
        parameter5.borderWidth = 3;
        parameter5.borderColor = Color.GRAY;
        bigFont = generator5.generateFont(parameter5);
        pause = new Image(new Texture("pause1.png"));
        pause.setPosition(Gdx.graphics.getWidth() - pause.getWidth() - 15, Gdx.graphics.getHeight() - pause.getHeight() - 15);

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
        turnLabel = new Label("     Your turn", style);
        turnLabel.setPosition(70, 100);
        turnLabel.setSize(250, 50);
        Pixmap labelColor = new Pixmap((int) turnLabel.getWidth(), (int) turnLabel.getHeight(), Pixmap.Format.RGB888);
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
        messageLabel = new Label("     Make your first step!", style2);
        messageLabel.setPosition(0, 0);
        messageLabel.setSize(Gdx.graphics.getWidth(), 40);
        Pixmap labelColor2 = new Pixmap((int) messageLabel.getWidth(), (int) messageLabel.getHeight(), Pixmap.Format.RGB888);
        labelColor2.setColor(Color.WHITE);
        labelColor2.fill();
        messageLabel.getStyle().background = new Image(new Texture(labelColor2)).getDrawable();
        stage = new Stage(new ScreenViewport());
        computerGround = new ComputerGround(10, 10, Gdx.graphics.getWidth() / 3 + 100, Gdx.graphics.getHeight() - 100, ground);
        computerGround.setSeaBattleGame(this.game);
        userGround = ground;
        ground.setNewPositions(60, Gdx.graphics.getHeight() - 100);
        batch = new SpriteBatch();
        sprite = new Sprite(new Texture(Gdx.files.internal("back5.jpg")));
        FreeTypeFontGenerator generator4 = new FreeTypeFontGenerator(Gdx.files.internal("Zyana.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter4 = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter4.size = (int) userGround.getCellHeight() - 2;
        parameter4.color = Color.BLACK;
        parameter4.borderWidth = 2;
        parameter4.borderColor = Color.GRAY;
        fontNumbers = generator4.generateFont(parameter4);
        sprite.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage.addActor(userGround);
        stage.addActor(computerGround);
        stage.addActor(turnLabel);
        stage.addActor(messageLabel);
        stage.addActor(pause);
        if (level > 1) {
            stage.addActor(bomb);
            stage.addActor(radar);
            if (numberOfBombs == 0)
                bomb.setColor(Color.GRAY);
            if (numberOfRadars == 0)
                radar.setColor(Color.GRAY);
        }
        pauseDialog = new PauseDialog("Pause", skin);
        pauseDialog.setVisible(false);
        stage.addActor(pauseDialog);
        expl = new Image(new Texture(Gdx.files.internal("explosion.png")));
        expl.setPosition(500, 500);
        stage.addActor(expl);
        expl.setVisible(false);

        wave = new Image(new Texture(Gdx.files.internal("wave.png")));
        wave.setPosition(waveX, waveY);
        wave.setVisible(false);

        stage.addActor(wave);

        Gdx.input.setInputProcessor(this);
    }

    public static class PauseDialog extends Dialog {

        public PauseDialog(String title, Skin skin) {
            super(title, skin);
        }

        public PauseDialog(String title, Skin skin, String windowStyleName) {
            super(title, skin, windowStyleName);
        }

        {
            text("Choose option");
            button("Menu");
            button("Restart");
            button("Continue");
        }

        @Override
        protected void result(Object object) {
            this.setVisible(false);
        }

        public PauseDialog(String title, WindowStyle windowStyle) {
            super(title, windowStyle);
        }
    }

    @Override
    public void show() {
    }

    public int checkForWin() {
        if (userGround.getScore() == userGround.getNumberOfCellsInRow()) {
            return -1;
        }
        if (computerGround.getGround().getScore() == userGround.getNumberOfCellsInRow()) {
            return 1;
        }
        return 0;
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);
        if (checkForWin() != 0) {
            if (checkForWin() == 1) {
                bonusScore = computerGround.getBonusScore();
                game.setTotalScore(game.getTotalScore() + computerGround.getBonusScore());
            }
            if (level < 3 || checkForWin() == -1)
                game.setScreen(new EndScreen(game, checkForWin(), computerGround.getGround().getScore(), level, bonusScore));
            else game.setScreen(new VictoryScreen(game));
            System.out.println(computerGround.getBonusScore());
        }
        if (waveStage != 0 && shouldWave == 1) {
            shouldWave=3;
            whoIsNext=3;
            System.out.println("WAVE");
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    System.out.println("In run wave");
                    String waveMessage = "";
                    waveAnimation(waveStage, whoWave);
                    waveStage++;
                    boolean second = false;
                    if (waveStage > 80) {
                        waveStage = 0;
                        wave.setVisible(false);
                        shouldWave = 0;

                        if(whoWave) {
                            waveMessage += " computer's";
                            for (Cell c : computerGround.chooseWaveCells()) {
                                if (computerGround.getGround().checkShotCell(c)) {
                                    computerGround.getGround().killCell(c);
                                    c.setColor(Color.GREEN);

                                } else {
                                    c.changeColor(Color.GRAY);
                                }
                                waveMessage += " " + computerGround.getGround().getCellName(c);
                                if(!second) waveMessage += " and";
                                else waveMessage+="!";
                                second = true;
                                c.setShot(true);

                            }
                            messageLabel.setText("  Wave damaged" + waveMessage);

                        }
                        else {
                            waveMessage += " user's";
                            for (Cell c : userGround.chooseWaveCells()) {
                                if (userGround.checkShotCell(c)) {
                                    if(userGround.killCell(c)) {
                                        game.setYourShipsKilled(game.getYourShipsKilled()+1);

                                    }
                                    computerGround.setBonusScore(computerGround.getBonusScore()-1);
                                } else {
                                    c.changeColor(Color.GRAY);

                                }
                                waveMessage += " " + userGround.getCellName(c);
                                if(!second) waveMessage += " and ";
                                else waveMessage+="!";
                                second = true;
                                c.setShot(true);

                            }
                            messageLabel.setText("  Wave damaged" + waveMessage);

                        }
                        whoIsNext=1;

                    }
                }
            }, waveDelay);
        }
        float delay = 1;
        if (pauseDialog.isVisible() == false) {
            if (whoIsNext == 1) {
                whoIsNext = 3;
                turnLabel.setText(" Computer turn");
                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        computerGround.shoot(messageLabel);
                        if (computerGround.isStrike())
                            whoIsNext = 1;
                        else {
                            turnLabel.setText("     Your turn");
                            whoIsNext = 0;
                        }
                        expl.setVisible(false);
                        waveStage = 0;

                    }
                }, delay);
            }
        }
        batch.begin();
        sprite.draw(batch);
        if (level > 1) {
            bigFont.draw(batch, numberOfRadars + "x", radar.getX() - 80, radar.getY() + radar.getHeight() / 2 + 20);
            bigFont.draw(batch, numberOfBombs + "x", bomb.getX() - 80, bomb.getY() + bomb.getHeight() / 2);
        }
        font.draw(batch, "Your field", userGround.getCell(0, 2).getX() + 10, Gdx.graphics.getHeight() - 20);
        font.draw(batch, "Computer field", computerGround.getGround().getCell(0, 2).getX() - 25, Gdx.graphics.getHeight() - 20);
        String letters = "ABCDEFGHIJ";
        fontNumbers.draw(batch, String.valueOf(letters.charAt(0)), userGround.getCell(0, 0).getX() + 10, Gdx.graphics.getHeight() - 67);
        fontNumbers.draw(batch, String.valueOf(userGround.getNumberOfCellsInRow()), 20, userGround.getCell(9, 0).getY() + userGround.getCellHeight() - 6);
        fontNumbers.draw(batch, String.valueOf(letters.charAt(0)), computerGround.getGround().getCell(0, 0).getX() + 10, Gdx.graphics.getHeight() - 67);
        fontNumbers.draw(batch, String.valueOf(userGround.getNumberOfCellsInRow()), computerGround.getGround().getCell(0, 0).getX() - 38, computerGround.getGround().getCell(9, 0).getY() + userGround.getCellHeight() - 6);
        for (int i = 1; i < userGround.getNumberOfCellsInRow(); i++) {
            fontNumbers.draw(batch, String.valueOf(userGround.getNumberOfCellsInRow() - i), 30, userGround.getCell(userGround.getNumberOfCellsInRow() - i - 1, 0).getY() + userGround.getCellHeight() - 6);
            fontNumbers.draw(batch, String.valueOf(letters.charAt(i)), userGround.getCell(0, i).getX() + 15, Gdx.graphics.getHeight() - 67);
            fontNumbers.draw(batch, String.valueOf(letters.charAt(i)), computerGround.getGround().getCell(0, i).getX() + 15, Gdx.graphics.getHeight() - 67);
            fontNumbers.draw(batch, String.valueOf(userGround.getNumberOfCellsInRow() - i), computerGround.getGround().getCell(0, 0).getX() - 28, computerGround.getGround().getCell(userGround.getNumberOfCellsInRow() - i - 1, 0).getY() + userGround.getCellHeight() - 6);
        }
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
        Actor hitActor2 = computerGround.getGround().getStage().hit(coord.x, coord.y, true);
        Actor hitActor = stage.hit(coord.x, coord.y, true);
        if (hitActor == pause && bonusChosen == 0) {
            System.out.println("Pause");
            pauseDialog.setVisible(true);
            pauseDialog.setZIndex(200);
            pauseDialog.show(stage);
        } else if (hitActor != null && pauseDialog.isVisible() == true && bonusChosen == 0) {
            if (hitActor.getClass() == Label.class && ((Label) hitActor).getText().length == 4) {
                game.setScreen(new MainMenu(game, level, bonusScore));
            } else if (hitActor.getClass() == Label.class && ((Label) hitActor).getText().length == 7) {
                game.setScreen(new PutShipsScreen(game, level, bonusScore));
            } else if (hitActor.getClass() == Label.class && ((Label) hitActor).getText().length == 8) {
                pauseDialog.setVisible(false);
            }
        } else if (level > 1 && hitActor == bomb && numberOfBombs > 0 && bonusChosen == 0) {
            bomb.setColor(Color.RED);
            bonusChosen = 2;
        } else if (level > 1 && hitActor == radar && numberOfRadars > 0 && bonusChosen == 0) {
            radar.setColor(Color.PINK);
            bonusChosen = 1;
        } else if (bonusChosen == 1 && hitActor == radar) {
            radar.setColor(Color.WHITE);
            bonusChosen = 0;
        } else if (bonusChosen == 2 && hitActor == bomb) {
            bomb.setColor(Color.WHITE);
            bonusChosen = 0;
        } else if (hitActor2 != null && bonusChosen == 1 && computerGround.getGround().getRadaredCells() != null) {
            /**радар був використаний**/
            game.setRadarsUsed(game.getRadarsUsed() + 1);
            Cell[] cells = computerGround.getGround().getRadaredCells();
            for (int i = 0; i < cells.length; i++) {
                if (!cells[i].isShot()) {
                    if (cells[i].getIsTaken()) {
                        cells[i].changeColor(Color.valueOf("3C5695"));
                    } else {
                        cells[i].changeColor(Color.valueOf("FBF6AD"));
                    }
                }
                cells[i].setRadared(true);
            }
            computerGround.getGround().setRadaredCells(null);
            bonusChosen = 0;
            radar.setColor(Color.WHITE);
            numberOfRadars--;
            whoIsNext = 1;
            if (numberOfRadars == 0)
                radar.setColor(Color.GRAY);
        } else if (hitActor2 != null && bonusChosen == 2 && computerGround.getGround().getBombedCells() != null) {

            /**бомба була використана**/
            game.setBombsUsed(game.getBombsUsed() + 1);
            LinkedList<Cell> cells = computerGround.getGround().getBombedCells();
            for (Cell cell : cells) {
                if (!cell.isShot() && !cell.getIsTaken()) {
                    cell.changeColor(Color.GRAY);
                    cell.setShot(true);
                } else if (!cell.isShot()) {
                    if (computerGround.getGround().killCell(cell)) {
                        messageLabel.setText("      User bombed " + computerGround.getGround().getCellName(cell) + " and killed the ship!");
                    } else {
                        messageLabel.setText("      User bombed " + computerGround.getGround().getCellName(cell) + " and damaged the ship!");
                    }
                    cell.changeColor(Color.GREEN);
                }
            }
            computerGround.getGround().setBombedCells(null);
            bonusChosen = 0;
            bomb.setColor(Color.WHITE);
            numberOfBombs--;
            whoIsNext = 1;
            if (numberOfBombs == 0)
                bomb.setColor(Color.GRAY);
        } else if (hitActor2 != null && bonusChosen == 0) {
            if (whoIsNext == 0) {
                System.out.println("yes" + hitActor2.getName() + " " + hitActor2.getX() + " " + hitActor2.getY() + " class: " + hitActor2.getClass());

                /**рандомні події
                 * 1 - вибух на своєму полі
                 * 2 - хвиля**/
                if (level == 3) {
                    int willOcure = random.nextInt(10);

                    if (willOcure ==1) randomAction = random.nextInt(3) + 1;

                    else randomAction = 0;
                    if ((randomAction == 2) && userGround.getScore() == 9) randomAction = 1;
                    if ((randomAction == 3) && computerGround.getGround().getScore() == 9) randomAction = 1;

                    if(randomAction==1){
                        int i = computerGround.getGround().getI((Cell) hitActor2);
                        int j = computerGround.getGround().getJ((Cell) hitActor2);
                        if(userGround.getCell(i, j).isShot()){
                            randomAction=0;
                        }
                    }

                }


                if (hitActor2.getClass() == Cell.class && randomAction == 0) {

                    if (!((Cell) hitActor2).isShot()) {
                        if (computerGround.getGround().checkShotCell((Cell) hitActor2)) {
                            if (computerGround.getGround().killCell((Cell) hitActor2)) {
                                messageLabel.setText("      User shot at " + computerGround.getGround().getCellName((Cell) hitActor2) + " and killed the ship!");
                            } else {
                                messageLabel.setText("      User shot at " + computerGround.getGround().getCellName((Cell) hitActor2) + " and damaged the ship!");
                            }
                            ((Cell) hitActor2).changeColor(Color.GREEN);
                        } else {
                            ((Cell) hitActor2).changeColor(Color.GRAY);
                            game.miss1.play();
                            ((Cell) hitActor2).setShot(true);
                            messageLabel.setText("      User shot at " + computerGround.getGround().getCellName((Cell) hitActor2) + " and missed.");
                            whoIsNext = 1;
                        }
                    }
                } else if (hitActor2.getClass() == Cell.class && randomAction == 1) {
                    if (!((Cell) hitActor2).isShot()) {
                    messageLabel.setText("  User tried to shoot, but missile detonated on their field!");
                    int i = computerGround.getGround().getI((Cell) hitActor2);
                    int j = computerGround.getGround().getJ((Cell) hitActor2);
                    expl.setPosition(userGround.getCell(i, j).getX(), userGround.getCell(i, j).getY());
                    expl.setVisible(true);
                    if (userGround.checkShotCell(userGround.getCell(i, j)) && !userGround.getCell(i, j).isShot()) {
                        if (userGround.killCell(userGround.getCell(i, j))) {
                            messageLabel.setText("  User tried to shoot at " + computerGround.getGround().getCellName((Cell) hitActor2) + ", but missile detonated on their field and killed the ship!");
                            //computerGround.getGround().setScore(computerGround.getGround().getScore() + 1);
                            computerGround.setBonusScore(computerGround.getBonusScore() - 1);
                        } else {
                            messageLabel.setText("  User tried to shoot at " + computerGround.getGround().getCellName((Cell) hitActor2) + ", but missile detonated on their field and damaged the ship!");
                            computerGround.setBonusScore(computerGround.getBonusScore() - 1);
                        }
                        if (!computerGround.getDamagedCells().isEmpty() && userGround.getCell(i, j).getBoat() != null) {
                            if (userGround.getCell(i, j).getBoat().equals(computerGround.getDamagedCells().get(0).getBoat()))
                                computerGround.getDamagedCells().clear();
                        }
                        computerGround.setBonusScore(computerGround.getBonusScore()-1);
                        whoIsNext = 1;
                    } else {
                        userGround.getCell(i, j).changeColor(Color.GRAY);
                        userGround.getCell(i, j).setShot(true);
                        messageLabel.setText("User tried to shoot at " + computerGround.getGround().getCellName((Cell) hitActor2) + ", but missile detonated on their field!");
                        whoIsNext = 1;
                    }
                    if(((Cell) hitActor2).isRadared() && ((Cell) hitActor2).getIsTaken())
                        ((Cell) hitActor2).changeColor(Color.valueOf("3C5695"));
                    else if(((Cell) hitActor2).isRadared())
                        ((Cell) hitActor2).changeColor(Color.valueOf("FBF6AD"));
                    else
                        ((Cell) hitActor2).changeColor(Color.WHITE);

                } }else if (hitActor2.getClass() == Cell.class && randomAction == 2) {
                    if (!((Cell) hitActor2).isShot()) {
                    messageLabel.setText("  Large wave came in, damaging computer's field!");
                    waveStage = 1;
                    shouldWave = 1;
                    whoIsNext = 1;
                    whoWave = true;
                    if(((Cell) hitActor2).isRadared() && ((Cell) hitActor2).getIsTaken())
                        ((Cell) hitActor2).changeColor(Color.valueOf("3C5695"));
                    else if(((Cell) hitActor2).isRadared())
                        ((Cell) hitActor2).changeColor(Color.valueOf("FBF6AD"));
                    else
                        ((Cell) hitActor2).changeColor(Color.WHITE);
                }}
                else if(hitActor2.getClass()==Cell.class && randomAction ==3) {
                    if (!((Cell) hitActor2).isShot()) {
                        messageLabel.setText("  Large wave came in, damaging your field!");
                        waveStage = 1;
                        shouldWave = 1;
                        whoIsNext = 1;
                        whoWave = false;
                        if (((Cell) hitActor2).isRadared() && ((Cell) hitActor2).getIsTaken())
                            ((Cell) hitActor2).changeColor(Color.valueOf("3C5695"));
                        else if (((Cell) hitActor2).isRadared())
                            ((Cell) hitActor2).changeColor(Color.valueOf("FBF6AD"));
                        else
                            ((Cell) hitActor2).changeColor(Color.WHITE);
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
        Vector2 coord = stage.screenToStageCoordinates(new Vector2((float) screenX, (float) screenY));
        Actor hitActor2 = computerGround.getGround().getStage().hit(coord.x, coord.y, true);
        if (!pauseDialog.isVisible() && bonusChosen == 0) {
            if (hitActor2 == null) {
                if (computerGround.getGround().getPaintedCell() != null && !computerGround.getGround().getPaintedCell().isShot()) {
                    if (computerGround.getGround().getPaintedCell().isRadared() && computerGround.getGround().getPaintedCell().getIsTaken()) {
                        computerGround.getGround().getPaintedCell().changeColor(Color.valueOf("3C5695"));
                    } else if (computerGround.getGround().getPaintedCell().isRadared()) {
                        computerGround.getGround().getPaintedCell().changeColor(Color.valueOf("FBF6AD"));
                    } else if (computerGround.getGround().getPaintedCell().getColor() != Color.GRAY) {
                        computerGround.getGround().getPaintedCell().changeColor(Color.WHITE);
                    }
                    computerGround.getGround().setPaintedCell(null);
                }
            } else {
                if (whoIsNext == 0) {
                    if (hitActor2.getClass() == Cell.class) {
                        if (computerGround.getGround().getPaintedCell() == null && !((Cell) hitActor2).isShot()) {
                            ((Cell) hitActor2).changeColor(Color.GREEN);
                            computerGround.getGround().setPaintedCell((Cell) hitActor2);
                        } else if (!((Cell) hitActor2).equals(computerGround.getGround().getPaintedCell()) && !((Cell) hitActor2).isShot()) {
                            if (!computerGround.getGround().getPaintedCell().isShot()) {
                                if (computerGround.getGround().getPaintedCell().isRadared() && computerGround.getGround().getPaintedCell().getIsTaken()) {
                                    computerGround.getGround().getPaintedCell().changeColor(Color.valueOf("3C5695"));
                                } else if (computerGround.getGround().getPaintedCell().isRadared()) {
                                    computerGround.getGround().getPaintedCell().changeColor(Color.valueOf("FBF6AD"));
                                } else {
                                    computerGround.getGround().getPaintedCell().changeColor(Color.WHITE);
                                }
                            }
                            ((Cell) hitActor2).changeColor(Color.GREEN);
                            computerGround.getGround().setPaintedCell((Cell) hitActor2);
                        }
                    }
                }
            }
        } else if (!pauseDialog.isVisible() && bonusChosen == 1) {
            if (hitActor2 == null) {
                if (computerGround.getGround().getRadaredCells() != null) {
                    changeCellsForRadar();
                    computerGround.getGround().setRadaredCells(null);
                }
            } else {
                if (hitActor2.getClass() == Cell.class) {
                    if (computerGround.getGround().getRadaredCells() == null) {
                        setCellsForRadar(hitActor2);
                    } else if (!((Cell) hitActor2).equals(computerGround.getGround().getMainRadaredCell())) {
                        changeCellsForRadar();
                        setCellsForRadar(hitActor2);
                    }
                }
            }
        } else if (!pauseDialog.isVisible() && bonusChosen == 2) {
            if (hitActor2 == null) {
                if (computerGround.getGround().getBombedCells() != null) {
                    changeCellsForBomb();
                    computerGround.getGround().setBombedCells(null);
                }
            } else {
                if (hitActor2.getClass() == Cell.class) {
                    if (computerGround.getGround().getBombedCells() == null) {
                        setCellsForBomb(hitActor2);
                    } else if (!((Cell) hitActor2).equals(computerGround.getGround().getMainBombedCell())) {
                        changeCellsForBomb();
                        setCellsForBomb(hitActor2);
                    }
                }
            }
        }
        return true;
    }

    private void changeCellsForBomb() {
        LinkedList<Cell> change = computerGround.getGround().getBombedCells();
        for (Cell cell : change) {
            if (cell.isRadared() && cell.getIsTaken() && !cell.isShot()) {
                cell.changeColor(Color.valueOf("3C5695"));
            } else if (cell.isRadared() && !cell.isShot()) {
                cell.changeColor(Color.valueOf("FBF6AD"));
            } else if (!cell.isShot())
                cell.changeColor(Color.WHITE);
        }
    }

    private void setCellsForBomb(Actor hitActor2) {
        LinkedList<Cell> cells = new LinkedList<>();
        cells.add((Cell) hitActor2);
        if (computerGround.getGround().getI((Cell) hitActor2) == 9 && computerGround.getGround().getJ((Cell) hitActor2) == 9) {
            cells.add(computerGround.getGround().getCell(8, 9));
            cells.add(computerGround.getGround().getCell(9, 8));
        } else if (computerGround.getGround().getI((Cell) hitActor2) == 0 && computerGround.getGround().getJ((Cell) hitActor2) == 9) {
            cells.add(computerGround.getGround().getCell(0, 8));
            cells.add(computerGround.getGround().getCell(1, 9));
        } else if (computerGround.getGround().getI((Cell) hitActor2) == 9 && computerGround.getGround().getJ((Cell) hitActor2) == 0) {
            cells.add(computerGround.getGround().getCell(9, 1));
            cells.add(computerGround.getGround().getCell(8, 0));
        } else if (computerGround.getGround().getI((Cell) hitActor2) == 0 && computerGround.getGround().getJ((Cell) hitActor2) == 0) {
            cells.add(computerGround.getGround().getCell(1, 0));
            cells.add(computerGround.getGround().getCell(0, 1));
        } else if (computerGround.getGround().getJ((Cell) hitActor2) == 9) {
            cells.add(computerGround.getGround().getCell(computerGround.getGround().getI((Cell) hitActor2), 8));
            cells.add(computerGround.getGround().getCell(computerGround.getGround().getI((Cell) hitActor2) - 1, 9));
            cells.add(computerGround.getGround().getCell(computerGround.getGround().getI((Cell) hitActor2) + 1, 9));
        } else if (computerGround.getGround().getJ((Cell) hitActor2) == 0) {
            cells.add(computerGround.getGround().getCell(computerGround.getGround().getI((Cell) hitActor2), 1));
            cells.add(computerGround.getGround().getCell(computerGround.getGround().getI((Cell) hitActor2) - 1, 0));
            cells.add(computerGround.getGround().getCell(computerGround.getGround().getI((Cell) hitActor2) + 1, 0));
        } else if (computerGround.getGround().getI((Cell) hitActor2) == 0) {
            cells.add(computerGround.getGround().getCell(0, computerGround.getGround().getJ((Cell) hitActor2) + 1));
            cells.add(computerGround.getGround().getCell(0, computerGround.getGround().getJ((Cell) hitActor2) - 1));
            cells.add(computerGround.getGround().getCell(1, computerGround.getGround().getJ((Cell) hitActor2)));
        } else if (computerGround.getGround().getI((Cell) hitActor2) == 9) {
            cells.add(computerGround.getGround().getCell(9, computerGround.getGround().getJ((Cell) hitActor2) + 1));
            cells.add(computerGround.getGround().getCell(9, computerGround.getGround().getJ((Cell) hitActor2) - 1));
            cells.add(computerGround.getGround().getCell(8, computerGround.getGround().getJ((Cell) hitActor2)));
        } else {
            cells.add(computerGround.getGround().getCell(computerGround.getGround().getI((Cell) hitActor2), computerGround.getGround().getJ((Cell) hitActor2) + 1));
            cells.add(computerGround.getGround().getCell(computerGround.getGround().getI((Cell) hitActor2), computerGround.getGround().getJ((Cell) hitActor2) - 1));
            cells.add(computerGround.getGround().getCell(computerGround.getGround().getI((Cell) hitActor2) + 1, computerGround.getGround().getJ((Cell) hitActor2)));
            cells.add(computerGround.getGround().getCell(computerGround.getGround().getI((Cell) hitActor2) - 1, computerGround.getGround().getJ((Cell) hitActor2)));
        }
        for (Cell cell : cells) {
            if (!cell.isShot())
                cell.changeColor(Color.valueOf("F1013E"));
        }
        computerGround.getGround().setBombedCells(cells);
    }

    private void setCellsForRadar(Actor hitActor2) {
        Cell[] cells = new Cell[4];
        cells[0] = (Cell) hitActor2;
        if (computerGround.getGround().getI(cells[0]) == 9 && computerGround.getGround().getJ(cells[0]) == 9) {
            cells[1] = computerGround.getGround().getCell(8, 9);
            cells[2] = computerGround.getGround().getCell(8, 8);
            cells[3] = computerGround.getGround().getCell(9, 8);
        } else if (computerGround.getGround().getI(cells[0]) == 0 && computerGround.getGround().getJ(cells[0]) == 9) {
            cells[1] = computerGround.getGround().getCell(0, 8);
            cells[2] = computerGround.getGround().getCell(1, 8);
            cells[3] = computerGround.getGround().getCell(1, 9);
        } else if (computerGround.getGround().getJ(cells[0]) == 9) {
            cells[1] = computerGround.getGround().getCell(computerGround.getGround().getI(cells[0]) - 1, 9);
            cells[2] = computerGround.getGround().getCell(computerGround.getGround().getI(cells[0]), 8);
            cells[3] = computerGround.getGround().getCell(computerGround.getGround().getI(cells[0]) - 1, 8);
        } else if (computerGround.getGround().getI(cells[0]) == 0) {
            cells[1] = computerGround.getGround().getCell(0, computerGround.getGround().getJ(cells[0]) + 1);
            cells[2] = computerGround.getGround().getCell(1, computerGround.getGround().getJ(cells[0]) + 1);
            cells[3] = computerGround.getGround().getCell(1, computerGround.getGround().getJ(cells[0]));
        } else {
            cells[1] = computerGround.getGround().getCell(computerGround.getGround().getI(cells[0]), computerGround.getGround().getJ(cells[0]) + 1);
            cells[2] = computerGround.getGround().getCell(computerGround.getGround().getI(cells[0]) - 1, computerGround.getGround().getJ(cells[0]) + 1);
            cells[3] = computerGround.getGround().getCell(computerGround.getGround().getI(cells[0]) - 1, computerGround.getGround().getJ(cells[0]));
        }
        for (int i = 0; i < cells.length; i++) {
            if (!cells[i].isShot())
                cells[i].changeColor(Color.valueOf("3C9556"));
        }
        computerGround.getGround().setRadaredCells(cells);
    }

    private void changeCellsForRadar() {
        Cell[] change = computerGround.getGround().getRadaredCells();
        for (int i = 0; i < change.length; i++) {
            if (change[i].isRadared() && change[i].getIsTaken() && !change[i].isShot()) {
                change[i].changeColor(Color.valueOf("3C5695"));
            } else if (change[i].isRadared() && !change[i].isShot()) {
                change[i].changeColor(Color.valueOf("FBF6AD"));
            } else if (!change[i].isShot())
                change[i].changeColor(Color.WHITE);
        }
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;

    }

    public void waveAnimation(final int i, boolean whoWave) {
        float delay = 0;
        /**комп'ютер**/
        if(whoWave==true) waveX = 600;
        else waveX = 60;

        waveDelay = Float.valueOf("0.01");
        if (shouldWave == 3) {
            shouldWave = 0;
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    shouldWave=1;
                    switch (i) {
                        case 1:
                            wave.setVisible(true);
                            wave.setPosition(waveX, waveY);
                            break;
                        default:
                            wave.setPosition(waveX, waveY - stepY * (i - 1));
                            break;
                    }

                }
            }, delay);
        }
    }

}
