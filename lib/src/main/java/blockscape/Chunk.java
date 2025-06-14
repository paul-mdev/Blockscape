package blockscape;

import com.jme3.asset.AssetManager;
import com.jme3.bounding.BoundingBox;
import com.jme3.collision.CollisionResults;
import com.jme3.material.Material;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.VertexBuffer;
import com.jme3.util.BufferUtils;

import java.util.List;
import java.util.ArrayList;

import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;

public class Chunk extends Node {

    public static final int WIDTH = 16;
    public static final int HEIGHT = 64;
    public static final int DEPTH = 16;
    public static final int SURFACE_LEVEL = 8;
    
    private Block[][][] blocks;
    private AssetManager assetManager;
    private Vector3f chunk_pos;

    private Geometry chunkGeometry;

    public Chunk(AssetManager assetManager, Vector3f chunk_pos) {
        this.assetManager = assetManager;
        this.chunk_pos = chunk_pos;
        setLocalTranslation(chunk_pos);
        blocks = new Block[WIDTH][HEIGHT][DEPTH];
        GenerateChunk();
        buildMesh();
    }

    private void GenerateChunk() {
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                for (int z = 0; z < DEPTH; z++) {
                    Vector3f position = new Vector3f(
                        x * Block.BLOCK_SIZE,
                        y * Block.BLOCK_SIZE,
                        z * Block.BLOCK_SIZE
                    );

                    
                    if (y < SURFACE_LEVEL && y > SURFACE_LEVEL - 3) {
                        blocks[x][y][z] = new Block("dirt", assetManager, position);
                    }
                    
                    else if (y == SURFACE_LEVEL) {
                        blocks[x][y][z] = new Block("grass", assetManager, position);
                    }
                    
                    else if ( y == (SURFACE_LEVEL + 1) && (int)(Math.random() * 50) == 1){
                    	GenerateTree(x, y, z);
                    }
                    
                    else if (y < SURFACE_LEVEL){
                        blocks[x][y][z] = new Block("stone", assetManager, position);
                    }
                    else {
                    	if (isAir(x,y,z))
                    		blocks[x][y][z] = null;
                    }
                }
            }
        }

    }
    
    private void GenerateTree(int x, int y, int z) {
    	// Générer le tronc
    	for (int log_y = 0; log_y < 5; log_y++ ) {
    		System.out.println(y + log_y);
    		if (isInsideChunk(x, y + log_y, z) && isAir(x, y + log_y,z)){
    			blocks[x][y + log_y][z] = new Block("log", assetManager, new Vector3f(x, y + log_y, z));
    		}	
    	}
    	
    	// Générer les feuilles
    	int leaf_height = 5;
    	int leaf_radius = 3;
    	for (int leaf_y = y + leaf_height; leaf_y < y + leaf_height + 5; leaf_y++) {
	    	for (int leaf_x = x - leaf_radius; leaf_x < x + leaf_radius; leaf_x++) {
	        	for (int leaf_z = z - leaf_radius; leaf_z < z + leaf_radius; leaf_z++) {
	        		if (isInsideChunk(leaf_x, leaf_y, leaf_z) && isAir(leaf_x, leaf_y , leaf_z)){
	        			blocks[leaf_x][leaf_y][leaf_z] = new Block("leaf", assetManager, new Vector3f((float)leaf_x, (float)leaf_y, (float)leaf_z));
	        		}
	        	}
	    	}
    	}
    }
    
    
    
    private boolean isInsideChunk(int x, int y, int z) {
        return x >= 0 && x < WIDTH
            && y >= 0 && y < HEIGHT
            && z >= 0 && z < DEPTH;
    }

    private boolean isAir(int x, int y, int z) {
        if (!isInsideChunk(x, y, z)) return true;  // hors chunk = air
        return blocks[x][y][z] == null;
    }

    
    
    public void buildMesh() {
        if (chunkGeometry != null) {
            this.detachChild(chunkGeometry);
        }

        Mesh mesh = new Mesh();

        List<Vector3f> vertices = new ArrayList<>();
        List<Integer> indices = new ArrayList<>();
        List<Vector2f> texCoords = new ArrayList<>();

        float s = Block.BLOCK_SIZE/2;

        int[] vertexCountRef = new int[]{0};

        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                for (int z = 0; z < DEPTH; z++) {
                    Block block = blocks[x][y][z];
                    if (block == null) continue;

                    Vector3f basePos = new Vector3f(x * s * 2, y * s * 2, z * s * 2);

                    if (isAir(x, y, z + 1)) addFace(Face.FRONT, basePos, s, block, vertices, indices, texCoords, vertexCountRef);
                    if (isAir(x, y, z - 1)) addFace(Face.BACK, basePos, s, block, vertices, indices, texCoords, vertexCountRef);
                    if (isAir(x + 1, y, z)) addFace(Face.RIGHT, basePos, s, block, vertices, indices, texCoords, vertexCountRef);
                    if (isAir(x - 1, y, z)) addFace(Face.LEFT, basePos, s, block, vertices, indices, texCoords, vertexCountRef);
                    if (isAir(x, y + 1, z)) addFace(Face.TOP, basePos, s, block, vertices, indices, texCoords, vertexCountRef);
                    if (isAir(x, y - 1, z)) addFace(Face.BOTTOM, basePos, s, block, vertices, indices, texCoords, vertexCountRef);
                }
            }
        }

 
        mesh.setBuffer(VertexBuffer.Type.Position, 3, BufferUtils.createFloatBuffer(vertices.toArray(new Vector3f[0])));
        mesh.setBuffer(VertexBuffer.Type.Index, 3, BufferUtils.createIntBuffer(indices.stream().mapToInt(i -> i).toArray()));
        mesh.setBuffer(VertexBuffer.Type.TexCoord, 2, BufferUtils.createFloatBuffer(texCoords.toArray(new Vector2f[0])));

        mesh.updateBound();
        mesh.updateCounts();

        chunkGeometry = new Geometry("ChunkMesh", mesh);
        chunkGeometry.setModelBound(new BoundingBox());
        chunkGeometry.updateModelBound();
        
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setTexture("ColorMap", assetManager.loadTexture("textures/atlas.png"));
        chunkGeometry.setMaterial(mat);

        attachChild(chunkGeometry);
    }

    
    
    enum Face {
        FRONT, BACK, RIGHT, LEFT, TOP, BOTTOM
    }

    
    private void addFace(Face face, Vector3f basePos, float s, Block block,
        List<Vector3f> vertices, List<Integer> indices, List<Vector2f> texCoords, int[] vertexCountRef) {

		Vector3f[] faceVerts = getFaceVertices(face, basePos, s);
		for (Vector3f v : faceVerts) vertices.add(v);
		
		int vc = vertexCountRef[0];
		indices.add(vc);
		indices.add(vc + 1);
		indices.add(vc + 2);
		indices.add(vc + 2);
		indices.add(vc + 3);
		indices.add(vc);
		
		Vector2f uvOffset = block.getUVOffset();
		float tileSize = 0.25f;
		
		texCoords.add(uvOffset.add(new Vector2f(0 * tileSize, 0 * tileSize)));
		texCoords.add(uvOffset.add(new Vector2f(1 * tileSize, 0 * tileSize)));
		texCoords.add(uvOffset.add(new Vector2f(1 * tileSize, 1 * tileSize)));
		texCoords.add(uvOffset.add(new Vector2f(0 * tileSize, 1 * tileSize)));
		
		vertexCountRef[0] += 4;
	}
	    
	    
    
    
    
    private Vector3f[] getFaceVertices(Face face, Vector3f p, float s) {
        switch (face) {
            case FRONT:
                return new Vector3f[] {
                    new Vector3f(p.x - s, p.y - s, p.z + s),
                    new Vector3f(p.x + s, p.y - s, p.z + s),
                    new Vector3f(p.x + s, p.y + s, p.z + s),
                    new Vector3f(p.x - s, p.y + s, p.z + s)
                };
            case BACK:
                return new Vector3f[] {
                    new Vector3f(p.x + s, p.y - s, p.z - s),
                    new Vector3f(p.x - s, p.y - s, p.z - s),
                    new Vector3f(p.x - s, p.y + s, p.z - s),
                    new Vector3f(p.x + s, p.y + s, p.z - s)
                };
            case RIGHT:
                return new Vector3f[] {
                    new Vector3f(p.x + s, p.y - s, p.z + s),
                    new Vector3f(p.x + s, p.y - s, p.z - s),
                    new Vector3f(p.x + s, p.y + s, p.z - s),
                    new Vector3f(p.x + s, p.y + s, p.z + s)
                };
            case LEFT:
                return new Vector3f[] {
                    new Vector3f(p.x - s, p.y - s, p.z - s),
                    new Vector3f(p.x - s, p.y - s, p.z + s),
                    new Vector3f(p.x - s, p.y + s, p.z + s),
                    new Vector3f(p.x - s, p.y + s, p.z - s)
                };
            case TOP:
                return new Vector3f[] {
                    new Vector3f(p.x - s, p.y + s, p.z + s),
                    new Vector3f(p.x + s, p.y + s, p.z + s),
                    new Vector3f(p.x + s, p.y + s, p.z - s),
                    new Vector3f(p.x - s, p.y + s, p.z - s)
                };
            case BOTTOM:
                return new Vector3f[] {
                    new Vector3f(p.x - s, p.y - s, p.z - s),
                    new Vector3f(p.x + s, p.y - s, p.z - s),
                    new Vector3f(p.x + s, p.y - s, p.z + s),
                    new Vector3f(p.x - s, p.y - s, p.z + s)
                };
            default:
                return new Vector3f[0];
        }
    }

    
    
    
    
    
    
    public Block getBlock(int x, int y, int z) {
        if (isInsideChunk(x, y, z)) {
            return blocks[x][y][z];
        }
        return null;
    }

    public boolean setBlock(int x, int y, int z, Block block) {
        if (!isInsideChunk(x, y, z)) return false;

        blocks[x][y][z] = block;
        buildMesh();  // reconstruire le mesh à chaque changement (lent, mais simple pour commencer)
        return true;
    }
    
    
    
    
    
    
    
    
    
    public Geometry getHitBlock(Vector3f origin, Vector3f direction) {
        Ray ray = new Ray(origin, direction);
        CollisionResults results = new CollisionResults();
       
        this.collideWith(ray, results);  

        if (results.size() > 0) {
            return results.getClosestCollision().getGeometry();
        }
        return null;
    }

    
    
    
    public Geometry getChunkGeometry() {
        return chunkGeometry;
    }

    
    
    
    
}
