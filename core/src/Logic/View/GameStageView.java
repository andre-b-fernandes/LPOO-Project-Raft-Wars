package Logic.View;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import java.util.ArrayList;
import Logic.Body.GameStageController;
import Logic.Model.Game;
import Logic.Model.GameStage;
import Logic.Model.SecondMap;
import Logic.Model.ThirdMap;


/**
 * Created by André on 30-04-2017.
 */

public class GameStageView extends ScreenAdapter implements InputProcessor{
    public boolean gyroscopeAvail;
    private BitmapFont player1Score;
    private BitmapFont player2Score;
    private Texture health;
    private Texture backImage;
    private Texture obstacle;
    private ArrayList<CharacterView> heroesPlayer1 = new ArrayList<CharacterView>();
    private ArrayList<CharacterView> heroesPlayer2 = new ArrayList<CharacterView>();
    private Matrix4 debugMatrix;
    private Box2DDebugRenderer debugRenderer;
    private Vector2 lastTouch;
    public static final float PIXEL_TO_METER = 50f;
    public static float VIEWPORT_WIDTH;
    public static float VIEWPORT_HEIGHT;
    private OrthographicCamera camera;
    public int c;
    private static final float MIN_HEIGHT = 87.5f;
    private static final float MIN_WIDTH = 87.5f;
    private static final float CAMERA_SIZE_MULTIPLIER = 3f;

    public void loadAssets(){
        Game.getInstance().getAssetManager().load("heart.png", Texture.class);
        Game.getInstance().getAssetManager().load("background1.jpg", Texture.class);
        Game.getInstance().getAssetManager().load("background2.png", Texture.class);
        Game.getInstance().getAssetManager().load("background3.png", Texture.class);
        Game.getInstance().getAssetManager().load("reviveddragon.png", Texture.class);
        Game.getInstance().getAssetManager().load("wyvernfire.png", Texture.class);
        Game.getInstance().getAssetManager().load("wyvernwater.png", Texture.class);
        Game.getInstance().getAssetManager().load("ballwater.png", Texture.class);
        Game.getInstance().getAssetManager().load("ballfire.png", Texture.class);
        Game.getInstance().getAssetManager().load("rock.png", Texture.class);
        Game.getInstance().getAssetManager().finishLoading();
    }

    public void assetsCreator(){
        backImage = Game.getInstance().getAssetManager().get("background1.jpg");
        health = Game.getInstance().getAssetManager().get("heart.png");
        obstacle = Game.getInstance().getAssetManager().get("rock.png");
        VIEWPORT_WIDTH = backImage.getWidth();
        VIEWPORT_HEIGHT = backImage.getHeight();
    }

    public GameStageView() {
        c = 0;
        loadAssets();
        this.assetsCreator();
        lastTouch = new Vector2();
        gyroscopeAvail = Gdx.input.isPeripheralAvailable(Input.Peripheral.Gyroscope);
        for (int i = 0; i < GameStageController.getInstance().getBodiesPlayer1().size(); i++) {
            String ammoFilename = GameStage.getInstance().getHeroesPlayer1().get(i).getAmmo().getFilename();
            heroesPlayer1.add(new CharacterView(GameStage.getInstance().getHeroesPlayer1().get(i).getFilename(), 8, 1, ammoFilename));
        }
        for (int i = 0; i < GameStageController.getInstance().getBodiesPlayer2().size(); i++) {
            String ammoFilename = GameStage.getInstance().getHeroesPlayer2().get(i).getAmmo().getFilename();
            heroesPlayer2.add(new CharacterView(GameStage.getInstance().getHeroesPlayer2().get(i).getFilename(),8, 1, ammoFilename));
        }
        this.player1Score = new BitmapFont();
        this.player2Score = new BitmapFont();
        loadAssets();
        camera = createCamera();
        Gdx.input.setInputProcessor(this);
    }

    OrthographicCamera createCamera() {
        OrthographicCamera camera = new OrthographicCamera(MIN_WIDTH*CAMERA_SIZE_MULTIPLIER , MIN_WIDTH* CAMERA_SIZE_MULTIPLIER * ((float) Gdx.graphics.getHeight() / (float) Gdx.graphics.getWidth()));
        camera.update();
        debugRenderer = new Box2DDebugRenderer();
        return camera;
    }

    public void checkBackImage(){
        if(GameStage.getInstance().isChangedLevel())
        {
            if(GameStage.getInstance().getGameLevel() instanceof SecondMap){
                backImage = Game.getInstance().getAssetManager().get("background2.png");
                VIEWPORT_WIDTH = backImage.getWidth();
                VIEWPORT_HEIGHT = backImage.getHeight();
            }
            else if(GameStage.getInstance().getGameLevel() instanceof ThirdMap){
                backImage = Game.getInstance().getAssetManager().get("background3.png");
                VIEWPORT_WIDTH = backImage.getWidth();
                VIEWPORT_HEIGHT = backImage.getHeight();
            }
            GameStageController.getInstance().reset();
        }
    }

    public void drawHealthBar1(int i){
        for(int c = 0; c < GameStage.getInstance().getHeroesPlayer1().get(i).getHp(); c++){
            Game.getInstance().getBatch().draw(health, this.getHeroesPlayer1().get(i).getSprite().getX() + 10*c + this.getHeroesPlayer1().get(i).getSprite().getWidth()/2, this.getHeroesPlayer1().get(i).getSprite().getY() + this.getHeroesPlayer1().get(i).getSprite().getHeight(),10,10);
        }
    }

    public void drawHealthBar2(int i){
        for(int c = 0; c < GameStage.getInstance().getHeroesPlayer2().get(i).getHp(); c++){
            Game.getInstance().getBatch().draw(health, this.getHeroesPlayer2().get(i).getSprite().getX() + 10*c + this.getHeroesPlayer2().get(i).getSprite().getWidth()/2, this.getHeroesPlayer2().get(i).getSprite().getY() + this.getHeroesPlayer2().get(i).getSprite().getHeight(),10,10);
        }
    }

    public void drawPlayers(){
        for (int i = 0; i < heroesPlayer1.size(); i++) {
            if(GameStage.getInstance().getHeroesPlayer1().get(i).isActive()) {
                this.drawHealthBar1(i);
                heroesPlayer1.get(i).getSprite().flip(true, false);
                heroesPlayer1.get(i).draw(Game.getInstance().getBatch());
            }
        }
        for (int i = 0; i < heroesPlayer2.size(); i++) {
            if(GameStage.getInstance().getHeroesPlayer2().get(i).isActive()) {
                this.drawHealthBar2(i);
                heroesPlayer2.get(i).draw(Game.getInstance().getBatch());
            }
        }
    }

    public void drawScene(){
        Game.getInstance().getBatch().draw(backImage, 0, 0, VIEWPORT_WIDTH,VIEWPORT_HEIGHT);
        Game.getInstance().getBatch().draw(obstacle, GameStageController.getInstance().getMiddleObstacle().getPosition().x * PIXEL_TO_METER -obstacle.getWidth() / 8, GameStageController.getInstance().getMiddleObstacle().getPosition().y * PIXEL_TO_METER  - obstacle.getHeight() / 8, obstacle.getWidth() / 4 , obstacle.getHeight() / 4);
        if(GameStage.getInstance().getPlayerTurn() == 1){
            player1Score.draw(Game.getInstance().getBatch(), "SCORE: " + Integer.toString(GameStage.getInstance().getPlayer1Score()), camera.position.x - camera.viewportHeight/2, camera.position.y + camera.viewportHeight/2);
        }
        else if(GameStage.getInstance().getPlayerTurn() == 2){
            player2Score.draw(Game.getInstance().getBatch(), "SCORE: " + Integer.toString(GameStage.getInstance().getPlayer2Score()), camera.position.x - camera.viewportHeight/2, camera.position.y + camera.viewportHeight/2);
        }
        this.drawPlayers();
    }

    private static boolean aux = false;

    public void accel(){

       if(this.gyroscopeAvail){
           float gyroX = Gdx.input.getGyroscopeX();
           if(gyroX <= -5 && aux == false)
           {
               aux = true;
           }
           else if(gyroX >= 5 && aux == true){
               GameStage.getInstance().changeSelected(c);
               aux = false;
           }
       }
    }

    public void posCamera(){
        float yValue = 0;
        float xValue = 0;
        accel();
        if(GameStage.getInstance().getPlayerTurn() == 1)
        {
            yValue = heroesPlayer1.get(c).getAmmoView().getSprite().getY();
            xValue = heroesPlayer1.get(c).getAmmoView().getSprite().getX();
        }
        else if(GameStage.getInstance().getPlayerTurn() == 2)
        {
            yValue = heroesPlayer2.get(c).getAmmoView().getSprite().getY();
            xValue = heroesPlayer2.get(c).getAmmoView().getSprite().getX();
        }
        if(yValue < MIN_HEIGHT) {
            yValue = MIN_HEIGHT;
        }
        else if (yValue > VIEWPORT_HEIGHT - MIN_HEIGHT){
            yValue = VIEWPORT_HEIGHT - MIN_HEIGHT;
        }
        if(xValue < MIN_WIDTH * CAMERA_SIZE_MULTIPLIER/2f){
            xValue = MIN_WIDTH * CAMERA_SIZE_MULTIPLIER/2f;
        }
        else if(xValue > VIEWPORT_WIDTH - MIN_WIDTH * CAMERA_SIZE_MULTIPLIER/2f){
            xValue = VIEWPORT_WIDTH - MIN_WIDTH * CAMERA_SIZE_MULTIPLIER/2f; //3/2
        }
        camera.position.set(xValue,yValue,0);
    }

    @Override
    public void render(float delta) {
        this.updateView(delta);
        Gdx.gl.glClearColor( 1, 1, 1, 1 );
        Gdx.gl.glClear( GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT );
        this.posCamera();
        camera.update();
        Game.getInstance().getBatch().setProjectionMatrix(camera.combined);
        debugMatrix = Game.getInstance().getBatch().getProjectionMatrix().cpy().scale(PIXEL_TO_METER, PIXEL_TO_METER, 0);
        Game.getInstance().getBatch().begin();
        this.drawScene();
        Game.getInstance().getBatch().end();
        //debugRenderer.render(GameStageController.getInstance().getWorld(), debugMatrix);
    }

    public void updateView(float delta) {
        this.checkBackImage();
        GameStageController.getInstance().update();
        c = GameStage.getInstance().getSelectedCharacter();
        for (int i = 0; i < this.getHeroesPlayer1().size(); i++) {
            this.getHeroesPlayer1().get(i).update(delta, GameStageController.getInstance().getBodiesPlayer1().get(i));
            this.getHeroesPlayer1().get(i).getAmmoView().update(delta, GameStageController.getInstance().getBodiesPlayer1().get(i).getAmmoBody());
        }
        for (int i = 0; i < this.getHeroesPlayer2().size(); i++) {
            this.getHeroesPlayer2().get(i).update(delta, GameStageController.getInstance().getBodiesPlayer2().get(i));
            this.getHeroesPlayer2().get(i).getAmmoView().update(delta, GameStageController.getInstance().getBodiesPlayer2().get(i).getAmmoBody());
        }

    }

    public ArrayList<CharacterView> getHeroesPlayer1() {
        return heroesPlayer1;
    }


    public ArrayList<CharacterView> getHeroesPlayer2() {
        return heroesPlayer2;
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
        Vector3 v3 = new Vector3(screenX, screenY, 0);
        v3 = camera.unproject(v3);
        lastTouch.set(v3.x, v3.y);
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        Vector3 aux = new Vector3(screenX, screenY, 0);
        aux = camera.unproject(aux);
        Vector2 newTouch = new Vector2(aux.x, aux.y);
        Vector2 delta = newTouch.cpy().sub(lastTouch);
        if((lastTouch.x >= this.getHeroesPlayer1().get(c).getAmmoView().getSprite().getX() && lastTouch.x <=  this.getHeroesPlayer1().get(c).getAmmoView().getSprite().getX() +  this.getHeroesPlayer1().get(c).getAmmoView().getSprite().getWidth()
                && lastTouch.y >= this.getHeroesPlayer1().get(c).getAmmoView().getSprite().getY() && lastTouch.y <= this.getHeroesPlayer1().get(c).getAmmoView().getSprite().getY() + this.getHeroesPlayer1().get(c).getAmmoView().getSprite().getHeight() && GameStage.getInstance().getPlayerTurn() ==1) ||
                (lastTouch.x >= this.getHeroesPlayer2().get(c).getAmmoView().getSprite().getX() && lastTouch.x <=  this.getHeroesPlayer2().get(c).getAmmoView().getSprite().getX() +  this.getHeroesPlayer2().get(c).getAmmoView().getSprite().getWidth()
                        && lastTouch.y >= this.getHeroesPlayer2().get(c).getAmmoView().getSprite().getY() && lastTouch.y <= this.getHeroesPlayer2().get(c).getAmmoView().getSprite().getY() + this.getHeroesPlayer2().get(c).getAmmoView().getSprite().getHeight() && GameStage.getInstance().getPlayerTurn() ==2))
        {
            GameStageController.getInstance().shootPlayerAmmo(delta.x/10, delta.y/10);
            lastTouch = newTouch;
            return true;
        }
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
    public boolean scrolled(int amount) {
        return false;
    }


}
