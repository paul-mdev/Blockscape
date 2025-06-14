package blockscape;

import com.jme3.app.SimpleApplication;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.font.BitmapText;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.system.AppSettings;
import com.jme3.math.FastMath;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;

public class Main extends SimpleApplication {
	private Chunk spawnChunk;
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
    	// Caméra
        flyCam.setEnabled(true);
        flyCam.setDragToRotate(false);  // rotation continue avec la souris
        flyCam.setMoveSpeed(10);

        cam.setLocation(new Vector3f(5, 10, 15));
        cam.lookAt(new Vector3f(5, 0, 5), Vector3f.UNIT_Y);
        
        
        // Ajouter un chunk
    	spawnChunk = new Chunk(assetManager, new Vector3f(0, 0, 0));
    	rootNode.attachChild(spawnChunk);

    	// Crosshair
    	BitmapText ch = new BitmapText(guiFont);

    	ch.setSize(guiFont.getCharSet().getRenderedSize() * 2); // taille du texte
    	ch.setText("+");
    	ch.setLocalTranslation(
    	    settings.getWidth() / 2 - ch.getLineWidth() / 2,
    	    settings.getHeight() / 2 + ch.getLineHeight() / 2,
    	    0);
    	guiNode.attachChild(ch);

    	// Action listener pour casser un bloc
    	inputManager.addMapping("BreakBlock", new KeyTrigger(KeyInput.KEY_F));
    	inputManager.addListener(actionListener, "BreakBlock");

    }
    
    public static Vector3f getTargetBlock(Vector3f point, Vector3f normal, float blockSize) {
        // On décale légèrement le point dans la direction opposée de la normale
        Vector3f adjustedPoint = point.subtract(normal.mult(0.01f));

        int x = (int) FastMath.floor(adjustedPoint.x / blockSize);
        int y = (int) FastMath.floor(adjustedPoint.y / blockSize);
        int z = (int) FastMath.floor(adjustedPoint.z / blockSize);

        return new Vector3f(x, y, z);
    }



    
    
    
    private ActionListener actionListener = new ActionListener() {
        public void onAction(String name, boolean isPressed, float tpf) {
            if (name.equals("BreakBlock") && isPressed) {

            	
            	CollisionResults results = new CollisionResults();
            	Ray ray = new Ray(cam.getLocation(), cam.getDirection());

            	spawnChunk.getChunkGeometry().collideWith(ray, results);

            	if (results.size() > 0) {
            		CollisionResult closest = results.getClosestCollision();
            		Vector3f contactPoint = closest.getContactPoint();
            		Vector3f normal = closest.getContactNormal();

            		System.out.println("ContactPoint: " + contactPoint + " Normal: " + normal);

            		Vector3f blockCoords = getTargetBlock(contactPoint, normal, Block.BLOCK_SIZE);
            		int x = (int) blockCoords.x;
            		int y = (int) blockCoords.y;
            		int z = (int) blockCoords.z;

      

            		System.out.println("Bloc touché aux coordonnées : " + x + ", " + y + ", " + z);

            		boolean blockGotDestroyed = spawnChunk.setBlock(x, y, z, null);
            		System.out.println(blockGotDestroyed);

            	    
            	    
            	    
            	    
           
            	}
            }
        }
    };
}
