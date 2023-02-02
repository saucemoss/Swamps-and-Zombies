package tile;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

import main.AnimationManager;
import main.GamePanel;
import pathfinding.ExampleFactory;
import pathfinding.Map;
import pathfinding.Node;

public class TileManager {
	GamePanel gp;
	public Tile[] tile;
	public int mapTileNum[][];
	public Map<Node> myMap;
	private int tileAnimCounter = 0;
	public boolean drawPath = true;
	ArrayList<Integer> tileToAnimPosX = new ArrayList<>();
	ArrayList<Integer> tileToAnimPosY = new ArrayList<>();
	Random random = new Random();
	public int dimension;

	public TileManager(GamePanel gp) {
		this.gp = gp;
		tile = new Tile[100];
		loadAnimationTiles();
		getTileImage();
		mapTileNum = new int[gp.maxWorldCol][gp.maxWorldRow];
		loadMap("/maps/map2.csv", 100);

	}

	private void loadAnimationTiles() {
		gp.animManager.loadSwampBubblingTileAnim();
		gp.animManager.loadSwampCactusMovingGreen();
		gp.animManager.loadTilesFromSpriteSheet();

	}

	public void loadMap(String path, int dimension) {
		this.dimension = dimension;
		mapTileNum = new int[dimension][dimension];
		try {
			InputStream is = getClass().getResourceAsStream(path);
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			myMap = new Map<>(dimension, dimension, new ExampleFactory());
			int col = 0;
			int row = 0;
			while (col < dimension && row < dimension) {
				String line = br.readLine();
				while (col < dimension) {
					String numbers[] = line.split(",");
					int num = Integer.parseInt(numbers[col]);
					mapTileNum[col][row] = num;
					if (tile[num].collision) {
						myMap.setWalkable(col, row, false);
					}
					col++;
				}
				if (col == dimension) {
					col = 0;
					row++;
				}

			}
//			myMap.setWalkable(48, 48, false);
//			myMap.drawMap();
			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void getTileImage() {

		setupTilesFromSpriteSheet(AnimationManager.tileSpritesArray);

	}

	private void setupTilesFromSpriteSheet(ArrayList<BufferedImage> spriteArray) {

		for (int i = 0; i < spriteArray.size(); i++) {
			// default tile
			tile[i] = new Tile();
			tile[i].image = spriteArray.get(i);
			tile[i].playerSpeed = 4;
			tile[i].collision = false;
			tile[i].projectilesGoThrough = true;
			tile[i].tileName = "placeholder name";

			switch (i) {
			// dirt tiles
			case 5: case 6: case 7: case 8: case 9: case 15:case 16:case 17:case 18:case 19: case 25: case 26: case 27:
				tile[i].tileName = "Dirt";
			break;
			// shootable-through, unwalkable tiles (collision on)

			case 0:
			case 1:
			case 2:
			case 3:
			case 4:
			case 10:
			case 12:
			case 13:
			case 14:
			case 20:
			case 21:
			case 22:
				tile[i].playerSpeed = 0;
				tile[i].collision = true;
				tile[i].projectilesGoThrough = true;
				break;
			// unshootable-through, unwalkable tiles (collision on)
			case 23:
			case 32:
			case 34:
				tile[i].playerSpeed = 0;
				tile[i].collision = true;
				tile[i].projectilesGoThrough = false;
				break;
			// 35-57 player-slowing tiles
			case 35:
			case 36:
			case 37:
			case 38:
			case 39:
			case 40:
			case 41:
			case 42:
			case 43:
			case 44:
			case 45:
			case 46:
			case 47:
			case 48:
			case 49:
			case 50:
			case 51:
			case 52:
			case 53:
			case 54:
			case 55:
			case 56:
			case 57:
			case 60:
			case 61:
			case 62:
				tile[i].playerSpeed = 3;
				tile[i].projectilesGoThrough = true;
				break;

			//platform
			case 31:
				tile[i].tileName = "Platform";
				break;

			// custom animated tiles:
			case 11:
				tile[i].tileName = "SwampBubbles";
				tile[i].playerSpeed = 0;
				tile[i].animated = true;
				tile[i].collision = true;
				tile[i].projectilesGoThrough = true;
				break;

			case 33:
				tile[i].tileName = "SwampCactusMovingGreen";
				tile[i].playerSpeed = 0;
				tile[i].collision = true;
				tile[i].animated = true;
				tile[i].projectilesGoThrough = false;
				break;
			}

		}

	}

	public Tile getTile(int num) {
		return tile[num];
	}

	public void update() {

		tileAnimCounter++;
		int col = 0;
		int row = 0;
		while (col < dimension && row < dimension) {
			int tileNum = mapTileNum[col][row];
			if (tileAnimCounter < 160) {
				if (tile[tileNum].animated) {
					switch (tile[tileNum].tileName) {
					case "SwampBubbles":
						int privCounter = tileAnimCounter;
						if (tileAnimCounter >= 80)
							privCounter -= 80;
						tile[tileNum].image = AnimationManager.swampTileAnimArray[privCounter / 8];
						break;
					case "SwampCactusMovingGreen":
						tile[tileNum].image = AnimationManager.swampCactusGreenMovingAnimArray[tileAnimCounter / 16];
						break;
					}

				}
			} else
				tileAnimCounter = 0;
			col++;
			if (col == dimension) {
				col = 0;
				row++;
			}
		}

	}

	public void draw(Graphics2D g2) {

		int col = 0;
		int row = 0;

		while (col < dimension && row < dimension) {
			int tileNum = mapTileNum[col][row];
			int worldX = col * gp.tileSize;
			int worldY = row * gp.tileSize;
			int screenX = worldX - gp.player.worldX + gp.player.screenX;
			int screenY = worldY - gp.player.worldY + gp.player.screenY;

			if ((worldX + gp.tileSize > gp.player.worldX - gp.player.screenX
					&& worldX - gp.tileSize < gp.player.worldX + gp.player.screenX
					&& worldY + gp.tileSize > gp.player.worldY - gp.player.screenY
					&& worldY - gp.tileSize < gp.player.worldY + gp.player.screenY)) {

				g2.drawImage(tile[tileNum].image, screenX, screenY, null);

				// g2.drawRect(screenX, screenY, gp.tileSize, gp.tileSize);
			}
			col++;

			if (col == dimension) {
				col = 0;
				row++;
			}
		}
		
		if(drawPath) {
			
			g2.setColor(new Color(0,200,0,100));
			
			for(int i = 0; i<gp.pathfinder.pathList.size(); i++) {
				int worldX = gp.pathfinder.pathList.get(i).col * gp.tileSize;
				int worldY = gp.pathfinder.pathList.get(i).row * gp.tileSize;
				int screenX = worldX - gp.player.worldX + gp.player.screenX;
				int screenY = worldY - gp.player.worldY + gp.player.screenY;
				
				g2.fillRect(screenX, screenY,gp.tileSize, gp.tileSize);
				
			}
		}
		
		// tile lookup
		// g2.drawImage(AnimationManager.tileSpritesArray.get(57), 500, 500,
		// gp.tileSize, gp.tileSize, null);

	}

}
