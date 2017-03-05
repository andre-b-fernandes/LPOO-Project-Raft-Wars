package dkeep.logic;

public class Suspicious extends Guard {

	public Suspicious(int x, int y, char im, int[][] m) {
		super(x, y, im, m);
	}

	@Override
	public void gMove(int contador) {
		//Falta mudar coisas
		/*
		if(movs[contador] == 'w')
		{
			//gPos[1] = gPos[1] - 1;
			this.setY(this.getPos()[1]-1);
		}
		else if(movs[contador] == 'a')
		{
			this.setX(this.getPos()[0]-1);
			//gPos[0] = gPos[0] - 1;
		}
		else if(movs[contador] == 's')
		{
			this.setY(this.getPos()[1]+1);
			//gPos[1] = gPos[1] + 1;
		}
		else if(movs[contador] == 'd')
		{
			this.setX(this.getPos()[0]+1);
			//gPos[0] = gPos[0] + 1;
		}	*/
		this.setX(movs[contador][0]); 
		this.setY(movs[contador][1]);
	}

}
