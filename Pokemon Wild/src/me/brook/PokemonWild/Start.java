package me.brook.PokemonWild;

import java.io.IOException;

public class Start {

	public static void main(String[] args) throws IOException {
		 PokemonWild wild = new PokemonWild();
		
		 Thread thread = new Thread(wild);
		 thread.start();
	}

}
