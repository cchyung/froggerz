package froggerz.game;

import java.util.Comparator;

public class SortSprite implements Comparator<SpriteComponent>{
	public int compare(SpriteComponent s1, SpriteComponent s2) {
		return s1.getDrawOrder() < s2.getDrawOrder() ? -1 : s1.getDrawOrder() > s2.getDrawOrder() ? +1 : 0;
	}
}
