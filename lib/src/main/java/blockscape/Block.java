package blockscape;

import com.jme3.material.Material;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.jme3.texture.Texture;
import com.jme3.asset.AssetManager;

public class Block {

    private Geometry geometry;
    public static final float BLOCK_SIZE = 0.5f;
    String name;
    Texture texture;
    float hardness;
    boolean isSolid;
    
 
    public Block(String name, AssetManager assetManager, Vector3f position) {
        this.name = name;
    	/*Box box = new Box(BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
        geometry = new Geometry("Block", box);
        geometry.setLocalTranslation(position);

        // Charger la texture dynamiquement
        Texture texture = assetManager.loadTexture("textures/" + name + ".png");

        // Créer un matériau Unshaded (sans lumière) avec la texture
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setTexture("ColorMap", texture);

        geometry.setMaterial(mat);
        */
        
    }

    
    
    public Vector2f getUVOffset() {
    	float factor = 0.25f;
    	switch (name) {
        	
            case "grass": return new Vector2f(0f, 3.0f * factor);
            case "stone": return new Vector2f(1.0f * factor, 3.0f * factor);
            //case "dirt":  return new Vector2f(1.0f * factor, 3.0f * factor);
            //case "sand":  return new Vector2f(2.0f * factor, 0f);
            default:      return new Vector2f(0f, 0f);
        }
    }

    
    
    /*public Geometry getGeometry() {
        return geometry;
    }*/
}
