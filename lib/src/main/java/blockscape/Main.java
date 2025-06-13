package blockscape;

import com.jme3.app.SimpleApplication;
import com.jme3.system.AppSettings;
import com.jme3.math.Vector3f;

public class Main extends SimpleApplication {

    public static void main(String[] args) {
    	// Lance le jeu
        Main app = new Main();
        AppSettings settings = new AppSettings(true);
        settings.setTitle("Blockscape");
        settings.setResolution(1280, 720);
        app.setSettings(settings);
        app.start();
    }

    @Override
    public void simpleInitApp() {
        flyCam.setEnabled(true);
        flyCam.setMoveSpeed(10); // vitesse de déplacement
        flyCam.setDragToRotate(true); // clique souris pour regarder
    	
    	Chunk spawnChunk = new Chunk(assetManager, new Vector3f(0, 0, 0));
    	rootNode.attachChild(spawnChunk);
    	/*
        int width = 10;
        int depth = 10;

        for (int x = 0; x < width; x++) {
            for (int z = 0; z < depth; z++) {
                Vector3f pos = new Vector3f(x, 0, z);
                Block block;
                if ((int)(Math.random() * 2)==1) {
                	block = new Block("grass", assetManager, pos);
                }
                else {
                	block = new Block("stone", assetManager, pos);
                }
                rootNode.attachChild(block.getGeometry());
            }
        }

        cam.setLocation(new Vector3f(5, 10, 15));
        cam.lookAt(new Vector3f(5, 0, 5), Vector3f.UNIT_Y);
    */}
}
