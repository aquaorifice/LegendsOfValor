//all movable object belongs to a role (Monster and hero)

public abstract class Role implements Cloneable{

	protected String name;

	//current HP
	protected int hp;

	// try to get connect with game
	static protected Game g;

	static protected int added;

	// max hp
	protected int maxHP;

	protected int level;

	//defence
	protected int def;
	protected static Map map;
	protected static Market market;

	private boolean isBattle = false;

	// save it location in its class
	protected int x;
	protected int y;

	private boolean hasWon = false;

	// the lane it belongs to
	protected int laneOri;

	// the lane it current in
	// count from 1      1,2,3
	protected int laneCurr;

	//to differentiate  display on  the map
	private static int num = 0;

	// the character to be displayed on the map
	protected char dis;


	// to describe whether the role has faint
	protected boolean wheFaint;
	public boolean getHasWon() {
		return hasWon;
	}

	public void setHasWon(boolean hasWon) {
		this.hasWon = hasWon;
	}

	public boolean getIsBattle() {
		return isBattle;
	}

	public void setIsBattle(boolean battle) {
		isBattle = battle;
	}
	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}


	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getLaneOri() {
		return laneOri;
	}

	public void setLaneOri(int laneOri) {
		this.laneOri = laneOri;
	}
	public int getLaneCurr() {
		return laneCurr;
	}

	public void setLaneCurr(int laneCurr) {
		this.laneCurr = laneCurr;
	}

	public static int getNum() {
		return num;
	}

	public static void setNum(int num) {
		Role.num = num;
	}

	public static void increaseNum() {
		num++;
	}

	public char getDis() {
		return dis;
	}

	public void setDis(char dis) {
		this.dis = dis;
	}

	public int getMaxHP() {
		return maxHP;
	}

	public Role(){}

	public Role(String na, int le, int de)
	{
//		num ++;
		//		dis = (char) (num+50);

		name = na;
		level = le;
		maxHP = hp = (level*100);
		def = de;
		wheFaint = false;
	}

	public boolean readyToDisplay(int xLoc,int yLoc){
		num++;
		x=xLoc;
		y = yLoc;
		dis = (char) (num+51);
		laneCurr = laneOri = y/3  +1;

		return true;
	}

	public void connectMap(Map m,Market mar,Game ga){
		g = ga;
		map = m;
		market = mar;
		System.out.println("after connect map: " +m);
	}



	@Override
	public String toString() {
		return "Role{" +
				"name='" + name + '\'' +
				", hp=" + hp +
				", level=" + level +
				", def=" + def +
				", wheFaint=" + wheFaint +
				'}';
	}

	//getter and setter below

	public String getName() {return name;}

	public int getLevel() {return level;}

	public int getHp() {return hp;}

	public int getDef() {return def;}

	public boolean getwheFaint() {return wheFaint;}

	public boolean setWheFaint( boolean whe) {wheFaint = whe;return wheFaint;}

	public void setName(String name) {
		this.name = name;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public void setHp(int hp) {
		this.hp = hp;
	}

	public void setDef(int def) {this.def = def;}

	public void becomeFaint() {
		wheFaint = true;
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
}
