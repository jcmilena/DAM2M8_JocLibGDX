package com.joandaustria.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;
import java.util.Random;

public class MyGdxGame extends ApplicationAdapter {
	SpriteBatch batch;
	Texture imgBomb, imgBG , imgPersonaje , imgMoneda;
	Texture [] animPerson;
	Rectangle rectPerson, rectBomb;
	int personY , bombaX , personFrame , contadorPerson , contadorBombas;
	int contadorMonedas;
	ArrayList<Integer> bombasXs , bombasYs;
	ArrayList<Integer> monedasXs , monedasYs;
	Rectangle manRectangle;
	ArrayList<Rectangle> bombsRectangles = new ArrayList<Rectangle>();
	ArrayList<Rectangle> monedasRectangles = new ArrayList<Rectangle>();

	Random random ;
	int score;
	int gameState;
	BitmapFont font;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		imgBomb = new Texture("bomb.png");
		imgMoneda = new Texture("coin.png");
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
		contadorMonedas = 0;

		rectPerson = new Rectangle();
		rectBomb = new Rectangle();
		bombasXs = new ArrayList<Integer>();
		bombasYs = new ArrayList<Integer>();
		monedasXs = new ArrayList<Integer>();
		monedasYs = new ArrayList<Integer>();
		random = new Random();
		score = 0;

		font = new BitmapFont();
		font.setColor(Color.WHITE);
		font.getData().scale(10);
	}

	@Override
	public void render () {

		//Dibujo entre batch.begin y batch.end
		batch.begin();


		if (gameState == 1){
			//Juego Activo
			//Ajustar tiempo y modificar frames Personaje
			contadorPerson++;
			contadorPerson = contadorPerson%10;
			if (contadorPerson == 0){
				personFrame++;
				personFrame=personFrame%4;
			}

			//Generar Bombas
			contadorBombas++;
			contadorBombas = contadorBombas%550;
			if(contadorBombas == 0){
				generarBomba();
			}

			//Generar Monedas
			contadorMonedas++;
			contadorMonedas = contadorMonedas%150;
			if(contadorMonedas == 0){
				generarMoneda();
			}



			//Equivalente al Listener de Android
			if(Gdx.input.isTouched()){
				personY+= 30;
			}


			if(personY > 0){
				personY-= 5;
			}



		} else if (gameState == 0){
			//Juego esperando inicio
			if(Gdx.input.isTouched()){
				gameState = 1;
				score = 0;
			}


		} else if (gameState == 2){
			// Fin de partida
			monedasXs.clear();
			monedasYs.clear();
			monedasRectangles.clear();
			bombasYs.clear();
			bombasXs.clear();
			bombsRectangles.clear();
			gameState = 0;
		}



		batch.draw(imgBG, 0, 0,Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		batch.draw(animPerson[personFrame],Gdx.graphics.getWidth() / 2 - 150, personY);
		manRectangle = new Rectangle(Gdx.graphics.getWidth() / 2 - 150,personY
				,imgPersonaje.getWidth(),imgPersonaje.getHeight());


		//Dibujar bombas y añadir rectangulo para colisiones
		bombsRectangles.clear();
		for(int i=0; i<bombasXs.size();i++)
		{
			batch.draw(imgBomb, bombasXs.get(i) , bombasYs.get(i));
			bombsRectangles.add(new Rectangle(bombasXs.get(i) , bombasYs.get(i), imgBomb.getWidth(), imgBomb.getHeight()));
			bombasXs.set(i,bombasXs.get(i)-5);

		}

		//Dibujar monedas y añadir rectangulo para colisiones
		monedasRectangles.clear();
		for(int i=0; i<monedasXs.size();i++)
		{
			batch.draw(imgMoneda, monedasXs.get(i) , monedasYs.get(i));
			monedasRectangles.add(new Rectangle(monedasXs.get(i) , monedasYs.get(i), imgBomb.getWidth(), imgBomb.getHeight()));
			monedasXs.set(i,monedasXs.get(i)-3);

		}



		// Detección colisión entre bomba i personaje
		for(int i = 0; i < bombsRectangles.size(); i++)
		{
			if(Intersector.overlaps(bombsRectangles.get(i), manRectangle)){
				bombasXs.remove(i);
				bombasYs.remove(i);
				bombsRectangles.remove(i);
				//sumar puntuación, parar partida,...
				gameState = 2;
				break;
			}
		}

		// Detección colisión entre moneda i personaje
		for(int i = 0; i < monedasRectangles.size(); i++)
		{
			if(Intersector.overlaps(monedasRectangles.get(i), manRectangle)){
				score++;
				monedasXs.remove(i);
				monedasYs.remove(i);
				monedasRectangles.remove(i);
				//sumar puntuación, parar partida,...
				break;
			}
		}


		// Dibujo marcador.
		font.draw(batch, String.valueOf(score), 100,200);

		batch.end();

	}

	private void generarBomba() {

		float altura = random.nextFloat() * Gdx.graphics.getHeight();
		bombasYs.add((int)altura);
		bombasXs.add(Gdx.graphics.getWidth());

	}

	private void generarMoneda() {

		float altura = random.nextFloat() * Gdx.graphics.getHeight();
		monedasYs.add((int)altura);
		monedasXs.add(Gdx.graphics.getWidth());


	}

	@Override
	public void dispose () {
		batch.dispose();

	}
}
