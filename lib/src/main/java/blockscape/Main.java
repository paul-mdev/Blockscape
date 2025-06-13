package blockscape;

import com.jme3.app.SimpleApplication;
import com.jme3.system.AppSettings;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;

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
        Box b = new Box(1, 1, 1); // create cube shape
        Geometry geom = new Geometry("Box", b);

        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.Blue);
        geom.setMaterial(mat);

        rootNode.attachChild(geom);
    }
}
