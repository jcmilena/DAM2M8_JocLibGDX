package com.joandaustria.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;
import java.util.Random;

public class MyGdxGame extends ApplicationAdapter {
	SpriteBatch batch;
	Texture imgBomb, imgBG , imgPersonaje;
	Texture [] animPerson;
	Rectangle rectPerson, rectBomb;
	int personY , bombaX , personFrame , contadorPerson , contadorBombas;
	ArrayList<Integer> bombasXs , bombasYs;

	Rectangle manRectangle;
	ArrayList<Rectangle> bombsRectangles = new ArrayList<Rectangle>();


	Random random ;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		imgBomb = new Texture("bomb.png");
		imgBG = new Texture("bg.png");
		imgPersonaje = new Texture("frame-1.png");
		animPerson = new Texture[4];
		animPerson[0] = new Texture("frame-1.png");
		animPerson[1] = new Texture("frame-2.png");
		animPerson[2] = new Texture("frame-3.png");
		animPerson[3] = new Texture("frame-4.png");




		personY = Gdx.graphics.getHeight() / 2 - 200;
		bombaX = Gdx.graphics.getWidth();
		personFrame = 0;
		contadorPerson = 0;
		contadorBombas = 0;

		rectPerson = new Rectangle();
		rectBomb = new Rectangle();
		bombasXs = new ArrayList<Integer>();
		bombasYs = new ArrayList<Integer>();
		random = new Random();
	}

	@Override
	public void render () {

		//Ajustar tiempo y modificar frames Personaje
		contadorPerson++;
		contadorPerson = contadorPerson%10;
		if (contadorPerson == 0){
			personFrame++;
			personFrame=personFrame%4;
		}

		//Generar Bombas
		contadorBombas++;
		contadorBombas = contadorBombas%500;
		if(contadorBombas == 0){
			generarBomba();
		}



		//Detectar colision Personaje - Bomba
		if(Intersector.overlaps(rectPerson, rectBomb)){

			//Juego se modifica con choque
			Gdx.app.log("Choque", "Bomba - person");
		}


		//Equivalente al Listener de Android
		if(Gdx.input.isTouched()){
			personY+= 30;
		}


		if(personY > 0){
			personY-= 5;
		}

		//Dibujo entre batch.begin y batch.end
		batch.begin();

		batch.draw(imgBG, 0, 0,Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		batch.draw(animPerson[personFrame],Gdx.graphics.getWidth() / 2 - 150, personY);
		manRectangle = new Rectangle(Gdx.graphics.getWidth() / 2 - 150,personY
				,imgPersonaje.getWidth(),imgPersonaje.getHeight());


		for(int i=0; i<bombasXs.size();i++)
		{
			batch.draw(imgBomb, bombasXs.get(i) , bombasYs.get(i));
			bombsRectangles.add(new Rectangle(bombasXs.get(i) , bombasYs.get(i), imgBomb.getWidth(), imgBomb.getHeight()));
			bombasXs.set(i,bombasXs.get(i)-1);

		}
		batch.end();

		for(int i = 0; i < bombsRectangles.size(); i++)
		{
			if(Intersector.overlaps(bombsRectangles.get(i), manRectangle)){
				bombasXs.remove(i);
				bombasYs.remove(i);
				bombsRectangles.remove(i);
				//sumar puntuación, parar partida,...
				break;
			}
		}


	}

	private void generarBomba() {

		float altura = random.nextFloat() * Gdx.graphics.getHeight();
		bombasYs.add((int)altura);
		bombasXs.add(Gdx.graphics.getWidth());


	}

	@Override
	public void dispose () {
		batch.dispose();

	}
}
