package com.Redream.TinyConflict;



import com.badlogic.gdx.math.Rectangle;

public interface InputListener {       
		public boolean touchCollTest();
		
		public Rectangle getBounds();
		
        public boolean keyDown(int keycode);

        public boolean keyUp(int keycode);
        
        public boolean keyTyped(char character);
        
        public boolean touchDown(int x,int y,int pointer);
        
        public boolean touchUp(int x,int y,int pointer);    
        
        public boolean touchDragged(int x, int y,int pointer);
        
        public boolean touchMoved(int x, int y);
        
        public boolean scrolled(int amount);
}