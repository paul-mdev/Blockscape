package blockscape;

import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.texture.Texture;
import com.jme3.asset.AssetManager;

public class Block {

    private Geometry geometry;
    public static final float BLOCK_SIZE = 1.0f;
    String name;
    Texture texture;
    float hardness;
    boolean isSolid;
    
 
    public Block(String name, AssetManager assetManager, Vector3f position) {
        this.name = name;
    }

    
    
    public Vector2f getUVOffset() {
    	float factor = 0.25f;
    	switch (name) {
        	
            case "grass": return new Vector2f(0f, 3.0f * factor);
            case "stone": return new Vector2f(1.0f * factor, 3.0f * factor);
            case "dirt":  return new Vector2f(1.0f * factor, 1.0f * factor);
            //case "sand":  return new Vector2f(2.0f * factor, 0f);
            
            case "log":  return new Vector2f(1.0f * factor, 0f);
            case "leaf":  return new Vector2f(2.0f * factor, 0f);
            default:      return new Vector2f(0f, 0f);
        }
    }


}
