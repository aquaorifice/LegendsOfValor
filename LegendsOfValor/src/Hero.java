import java.util.ArrayList;
import java.util.Scanner;
import java.lang.Math;
import java.util.Random;

//abstract class hero to be inheritenced
public abstract class Hero extends Role implements Cloneable{

	protected int mana;

	protected int maxMana;

	//alive heroes
	protected static ArrayList<Hero> heroAlive;

	//dead heroes
	protected static ArrayList<Hero> heroCorpse;

	private static ArrayList<Monster> monsterAlive;

	//strength
	protected int stren;
	
	//agility
	protected int agi;
	
	//dexterity
	protected int dex;
	
	protected int money;
	
	protected int exp;
	
	// the hands that a hero can use
	protected int hand = 2;
	
	protected String type;
	
	protected Weapon weap;
	
	protected Armour armor;

//	private temp =
	
	protected ArrayList<Armour> armorL;

	//market

	protected ArrayList<Weapon> weaponL;
	
	protected ArrayList<Potion> potionL;
	
	protected ArrayList<Spell> spellL;

	// this class can help to add or remove item
	protected ItemContainer itemContainer;

	protected Scanner scan;

	public Hero(String n, int le, int def,int ma,int st, int ag,int de,int m,int ex){

		super(n, le, def);

		potionL = new ArrayList<>();
		spellL = new ArrayList<>();
		armorL = new ArrayList<>();
		weaponL = new ArrayList<>();

		itemContainer =new ItemContainer(armorL,weaponL,potionL,spellL);

		maxMana = mana =ma;
		stren = st;
		agi = ag;
		dex =de;
		money = m;
		exp = ex;

		//try to cheat here
		hp*=64;
		maxHP = hp;
		money+=1024;
		level =1024;
		stren +=level;
		stren +=level;
		stren +=level;

		weap = null;
		armor = null;
		
		scan = new Scanner(System.in);

	}

    public Hero() {

    }

	//getter and setter Here
		public int getMana() {return mana;}

		public int getStren() {return stren;}

		public int getAgi() {return agi;}

		public int getDex() {return dex;}

		public int getMoney() {return money;}

		public int getExp() {return exp;}

		public int getHand() {return hand;}

		public String getType() {return type;}

		public Weapon getWeap() {return weap;}

		public Armour getArmor() {return armor;}

		public void setMana(int mana) {this.mana = mana;}

		public void setStren(int stren) {this.stren = stren;}

		public void setAgi(int agi) {this.agi = agi;}

		public void setDex(int dex) {this.dex = dex;}

		public void setMoney(int money) {this.money = money;}

		public void setExp(int exp) {this.exp = exp;}

		public void setHand(int hand) {this.hand = hand;}

		public void setWeap(Weapon weap) {this.weap = weap;}

		public void setArmor(Armour armor) {this.armor = armor;}

		public void setType(String type) {this.type = type;}

	public ArrayList<Armour> getArmour()
	{
		return armorL;
	}
	public ArrayList<Weapon> getWeaponry()
	{
		return weaponL;
	}
	public ArrayList<Potion> getPotionL()
	{
		return potionL;
	}
	public ArrayList<Spell> getSpellL()
	{
		return spellL;
	}

	public void setArmour(ArrayList<Armour> armors) {this.armorL = armors;}
	public void setPotionL(ArrayList<Potion> potionL) {this.potionL = potionL;}
	public void setSpellL(ArrayList<Spell> spellL) {this.spellL = spellL;}
	public void setWeaponry(ArrayList<Weapon> weapons) {this.weaponL = weapons;}


	// when hero attack the monster
	public void causeDMG(Monster mon){
		int dmg = 0;
		if (weap != null){
			// weapon will increase the damage
			dmg = (int) ((weap.getDamage() + stren) * 0.08);
		} else{
			dmg = (int) (stren * 0.08);
		}
		mon.loseHP(dmg);
		System.out.println("Hero " + name + " has caused " + dmg + " damage to monster " + mon.getName() + " .");
	}

	// heros can be hurt by monster
	public int loseHP(int dmg){
		int eff = 0;
		Random random = new Random();
		int temp = random.nextInt(10) + 1;

		if (temp < (int) (agi *0.0016)){
			System.out.println(name + " has successfully dodged an attack from the monster!");
			return eff;
		} else{
			if(armor ==null){
				eff = dmg;
			}else {
				eff = (int) (dmg - armor.getReductionRate()*0.04);

			}
			if (eff >= 0){
				hp -= eff;
			}
			// hero been killed
			if(hp <= 0){
				becomeFaint();
				heroAlive.remove(this);
				g.getHeroFighting().remove(this);
				System.out.println(g);
				heroCorpse.add(this);
				System.out.println("Oh no !!! Hero " + name + " has been eliminated");
				System.out.println("Oh no !!! Hero " + name + " has been diminished");
			}
		}
		return eff;
	}

	public int regainHpMana(){
		if(hp<maxHP){
			hp*= 1.1;
		}
		if(mana<maxMana){
			mana*=1.1;
		}

		return 0;
	}

	public boolean reSpawn(){

		boolean z=recall();

		if(z){
			wheFaint =false;
			System.out.println(getDis()+" has respawned");
		}else{
			System.out.println("The nexus has been occupied "+getDis()+" respawn failed	");
		}
		return z;
	}

	public static 	ArrayList<Hero> connectHeroParty(ArrayList<Hero> he,ArrayList<Hero> cor){

//		market=



//		monsterAlive = map.getMonsters();
		heroCorpse = cor;
		heroAlive = he;
		return heroAlive;
	}


	public char oneTurn(){
		// let player choose what to do next

		//check whether in a market
		if (map.grid[x][y].getType()=='n') {
			System.out.println("Hero " +getDis()+" You are now standing in a nexus. You can now buy and sell goods in the market.");
			market.enterMaket(this);
		}
		g.getMonsterAlive();

		System.out.println("zz");


		System.out.println("Hero " +getDis() +" What do you want to do next?");
		String instruction = "'q' to quit 'h' to display your information 'w' ,'a','s','d', to move 't' to teleport 'r' to recall 'p' to pass";
		char c = Helper.getCharInput(instruction);

		boolean ret = false;

//		if (checkMovementValidity(c)) {
			switch (c){
				case 'h':
					System.out.println(this);

					break;
				case 'q':
					System.out.println("Good bye Brave Hero! See you next time! Till then Live Long and Conquer!");
					System.exit(-16384);
				case 'r':
					ret = recall();
					break;
				case 't':
					ret = teleport();
					break;
				case 'w':
				case 'a':
				case 's':
				case 'd':
					ret = updateLocation(c);
					break;
				case 'p':
					ret = true;
//			}

		}

		if (ret) {
			return c;
		}else {
			System.out.println("The decision you've made isn't valid, please try again.");
			return oneTurn();
//						c = Helper.getCharInput(instruction);

		}
//		System.out.println();

		//main loop of the whole game
//		while (true){





//		}

//		return c;
	}

	public int getLaneOri(){
		return laneOri;
	}



	private boolean teleport() {

		int seq=0;

		for (Hero hero : heroAlive) {
			if (hero!=this) {
				if (laneCurr!= hero.laneCurr) {
					System.out.print("Do you want to move near" + hero.getDis()+" ? 'y' for yes 'n' for no");

					char r= Helper.getCharInput("'y' for yes 'n' for no");
					if (r=='y') {
						char sec = Helper.getCharInput("Where do you want to move? 'b' for beside 'a' for after");

						if (sec=='b') {
							int beside = (hero.y%3 %2 ==0 ?1:0) + (hero.laneCurr-1) * 3;
//							System.out.println("map:"+map);

							System.out.println("Beside:  "+beside+"y"+ map.grid[beside][hero.x].isHasHero()+" has hero.");
							System.out.println("hero location: "+hero.x +" " + hero.y);
							System.out.println("beside location: "+hero.x +" " + beside);
							System.out.println("my location: "+x +" " + y);
							if(!map.grid[beside][hero.x].isHasHero()){
//								System.out.println("Beside"+hero.getDis()+" there isn't any hero you can teleport");
								System.out.println(getDis()+" try to teleport beside "+hero.getDis());

								return leaveEnter(hero.x,beside);
//								return true;
							}else {
								System.out.println("Teleport failed,there already is a hero present there");
								return false;
							}
						}else {

							System.out.println(getDis()+" try to teleport behind "+hero.getDis());
							return leaveEnter(hero.x+1, hero.y);

						}


//						System.out.println("Where do you want to move? 'b' for beside 'a' for after");
					}
//					return true;
				}
			}
		}


		System.out.println("You didn't choose any hero destination to teleport to");
		return false;
	}


	// to be complete
	private boolean leaveEnter(int enterx,int entery){
		if(enterx == 0){
			System.out.println("The nexus for lane " +laneOri +" has been occupied by hero " +this.name);
			setHasWon(true);
		}
		try {
			char type= map.grid[enterx][entery].getType();
			if(type=='X'){
				System.out.println("The location you selected is an inaccessible area. You can't enter");
				return false;
			} else if (map.grid[enterx][entery].isHasHero()) {
				System.out.println("There is already a hero there! You can not enter it.");
				return false;
			}
			if(monsterAlive==null){

				monsterAlive = map.getMonsters();
			}

			for (Monster mon : monsterAlive) {
				if (mon.laneCurr==laneCurr) {
					if (enterx<mon.x) {
						System.out.println("You can not go to a location behind an existing monster!!!");
						return false;
					}
				}
			}


		} catch (Exception e) {
//			throw new RuntimeException(e);
			System.out.println(e.getMessage());
			System.out.println(e);
			System.out.println("The place you want to enter is out of bounds!");
			return false;
		}

//		System.out.println("Leave enter:");

//		System.out.println(getDis()+"Before x y:"+x+y);
		map.grid[x][y].setHasHero(false);
		map.grid[enterx][entery].setHasHero(true);
		//if hero leaves a bush then his dexterity is reduced to 100% instead of 110%
		if(map.grid[x][y].getType() == 'B'){
			dex = (int)Math.floor(dex/110 * 100);
		}
		//if hero leaves a cave then his agility is reduced to 100% instead of 110%
		else if(map.grid[x][y].getType() == 'C'){
			agi = (int)Math.floor(agi/110 * 100);
		}
		//if hero leaves a koulou then his dexterity is reduced to 100% instead of 110%
		else if(map.grid[x][y].getType() == 'K'){
			stren = (int)Math.floor(stren/110 * 100);
		}
		if(map.grid[enterx][entery].getType() == 'B'){
			dex =  (int)Math.ceil(1.1 * dex);
		}
		else if(map.grid[enterx][entery].getType() == 'C'){
			agi =  (int)Math.ceil(1.1 * agi);
		}
		else if(map.grid[enterx][entery].getType() == 'C'){
			stren = (int)Math.ceil(1.1 * stren);
		}

		x=enterx;
		y=entery;

		laneCurr = y/3 +1;

//		System.out.println(getDis()+"after x y: "+x+y);
		return true;
	}

	private boolean recall() {

		System.out.println("Try to recall to "+getDis()+"  home");
		return 	leaveEnter(7,(laneOri-1)*3);

	}

	private boolean checkMovementValidity(char move){


		switch (move){
			case 't':
				for (Hero hero : heroAlive) {
					if (hero!=this) {
						System.out.println("Lane:"+laneCurr +" "+ hero.laneCurr);
						if (laneCurr!= hero.laneCurr) {
							return true;
						}
					}
//					System.out.println();
					System.out.println("All heroes are in one lane!  No moves available");
				}return false;



		}


		return false;
	}

	private boolean updateLocation(char c) {

		boolean re=false;

		switch (c){
			case 'w':
				re = leaveEnter(x-1,y);
				break;
			case 's':
				re =leaveEnter(x+1,y);
				break;
			case 'a':
				re =leaveEnter(x,y-1);
				break;
			case 'd':
				re =leaveEnter(x,y+1);

		}

		return re;
	}

	public int getMaxMana() {
		return maxMana;
	}

	// basic levelup here
	public void levelUp()
	{
		maxMana = (int) Math.floor(maxMana*1.2);
		maxHP = (int) Math.floor(maxHP*1.2);
		regainHpMana();
		regainHpMana();
		regainHpMana();
		regainHpMana();
		exp -= level*64;
		if(exp < 0){
			exp =0;
		}
	}


	// when the hero receive item or drop item
	public Item recItem(Item e){
		itemContainer.recItem(e);
		return e;
	}
	public Item remoItem(Item e){
		itemContainer.removeItem(e);
		return e;
	}



	// heros can change their weapon
	public void changeCurrWeapon(){
		if (weaponL.size() == 0){
			System.out.println("You must own a weapon first to be able to equip a weapon");
		}else{
			System.out.println("Here is your weapon list, choose one to equip");

			System.out.println("+-------------------------------------------------------------------------------+");
			System.out.println("    Name     | Price  |  Level  |  Damage | Required_Hands");
			System.out.println("+-------------------------------------------------------------------------------+");
			// show all weapon
			for (Weapon w: weaponL){
				System.out.println(w);
			}
			System.out.println("+-------------------------------------------------------------------------------+");

			System.out.println("Choose the weapon you want using it's corresponding number ");
			int num = scan.nextInt();
			if(hand < weaponL.get(num-1).getHaNe()){
				System.out.println("Sorry! You do not seem to have enough free hands to equip this weapon!");
			}else {
				weap = weaponL.get(num-1);
				System.out.println("Successfully changed weapon!! Your current weapon is " + weap.getName());
			}
		}
	}

	// heros can change their armor
	public void changeCurrArmor(){
		if (armorL.size() == 0){
			System.out.println("You must own a armour first to be able to equip it");
		}else{
			System.out.println("This is your armour list. Choose one to equip.");
			System.out.println("+--------------------------------------------------------+");
			System.out.println("    Name     | Price  |  Level  |  Damage_Reduction ");
			System.out.println("+--------------------------------------------------------+");
			for (Armour a: armorL){
				System.out.println(a);
			}
			System.out.println("+----------------------------------------------------------+");

			System.out.println("Choose the armour you would like to equip using the corresponding number");
			int num = scan.nextInt();
			armor = armorL.get(num-1);
			System.out.println("Successfully changed armour!!Your current armor is " + armor.getName());
		}
	}

	// heros can use a potion
	public void usePotion(){
		if (potionL.size() == 0){
			System.out.println("You must own a potion first to be able to use it");
		}else{
			System.out.println("This is your potion list. Choose one to use.");

			System.out.println("+-----------------------------------------------------------------------+");
			System.out.println("    Name     | Price  |  Level  |  Increased_Value | Increased_Attribute ");
			System.out.println("+-----------------------------------------------------------------------+");
			// print the potion inventory of the hero
			for (Potion p: potionL){
				System.out.println(p);
			}

			System.out.println("+-----------------------------------------------------------------------+");

			System.out.println("Choose the potion you want using the corresponding number");
			int num = scan.nextInt();

			// deal with the effect of the potion
			if (potionL.get(num-1).getAttr().contains("Health")) {
				hp += potionL.get(num-1).getVal();
			}
			if (potionL.get(num-1).getAttr().contains("Strength")) {
				stren += potionL.get(num-1).getVal();
			}
			if (potionL.get(num-1).getAttr().contains("Mana")) {
				mana += potionL.get(num-1).getVal();
			}
			if (potionL.get(num-1).getAttr().contains("Agility")) {
				agi += potionL.get(num-1).getVal();
			}
			if (potionL.get(num-1).getAttr().contains("Dexterity")) {
				dex += potionL.get(num-1).getVal();
			}
			if (potionL.get(num-1).getAttr().contains("Defense")) {
				def += potionL.get(num-1).getVal();
			}

			System.out.println("Successfully used! Your " + potionL.get(num-1).getName() + " potion has come into effect!");
			potionL.remove(potionL.get(num-1));
		}
	}

	// heros can cast a spell
	public void useSpell(Monster mon) {
		if (spellL.size() == 0) {
			System.out.println("You must own a spell first to be able to use it");
		} else {
			System.out.println("Here is your spell list. Choose one to use.");
			System.out.println("+--------------------------------------------------------------+");
			System.out.println("    Name     | Price  |  Level  |  Damage | Mana_Cost | Type");
			System.out.println("+--------------------------------------------------------------+");
			// print the spell inventory of the hero
			for (Spell s : spellL) {
				System.out.println(s);
			}
			System.out.println("+---------------------------------------------------------------+");
			System.out.println("Choose the spell you want using the corresponding number");
			int num = scan.nextInt();
			if (spellL.get(num-1).getManaCost() > mana){
				System.out.println("You don't have enough mana to use this spell");
			}else{
				mana -= spellL.get(num-1).getManaCost();

				if (spellL.get(num-1).getType().equals("fire")){
					mon.setDmg((int)(mon.getDmg()*0.8));
				}
				else if(spellL.get(num-1).getType().equals("ice"))
					mon.setDef((int)(mon.getDef()*0.8));
				else
					mon.setDodge((int)(mon.getDodge()*0.8));

				int eff = (int) (spellL.get(num-1).getDamage()*(1 + dex / 10000));
				mon.loseHP(eff);
				System.out.println("Successfully used!!! You have used spell " + spellL.get(num-1).getName() + " on monster " + mon.getName() + " !");
				spellL.remove(spellL.get(num-1));
			}
		}
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {

		added++;

		y=added*3-3;
		x = 7;
		dis = (char) (added+48);
		laneCurr = laneOri = y/3  +1;

//		map.grid[x][y].setHasHero(true);


		System.out.printf("Clone function activated x: %d y:%d  dis:  %c  added: %d ",x,y,dis,added);
		System.out.println();


		return super.clone();
	}

	@Override
	public String toString() {


		String concate="";
		concate+=String.format("H%-22s",getDis());
		concate+=String.format("%-10s",getHp());
		concate+=String.format("%-10s",getLevel());
		concate+=String.format("%-10s",getMana());
		concate+=String.format("%-10s", getStren());
		concate+=String.format("%-10s", getAgi());
		concate+=String.format("%-10s", getDex());
		concate+=String.format("%-10s",getMoney());
		concate+=String.format("%-10s",getExp());
		concate+=String.format("%-10s",getType());
		concate+=String.format("%-10s", getwheFaint());


		return concate;
	}
}


