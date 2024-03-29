import java.util.*;


//the main control of the game
public class Game {
	
	
	//The hero list for initial choose
	private HeroList heroList;

	// contains all the monster which may occur
	private MonList monList;

	// the hero party class which are easier to control
	private HeroParty heroPP;

	// hero moving
	private ArrayList<Hero> heroMoving = new ArrayList<>();
	//hero fighting
	private ArrayList<Hero> heroFighting = new ArrayList<>();


	//monster alive
	private ArrayList<Monster> monsterAlive = new ArrayList<>();
	//hero corpses
	private ArrayList<Hero> heroCorpses;
	private ArrayList<Battle> battles = new ArrayList<>();
	private int roundsPlayed = 0;


	//market
	private Market market;
	//map
	private Map map;

	//when a hero encounter a monster
	private Combat combat;

	// Role list which contains of all of the monster and hero
	private ArrayList<Role> roles;

	private Scanner scan;
	private Random random;

	public Game() {
		heroList = new HeroList();
		monList = new MonList();
//		heroList.getHeroList().get(0).connectMap(map,market);

		market = new Market();
		map = new Map(8,setupRoles(), heroMoving,monsterAlive,this);
		scan = new Scanner(System.in);
		random = new Random();
	}


	// the main function of the game
	public void init() {
		// briefly explain the rule
		System.out.println("Hello there!!! Welcome to \" Legends of Valor \" ");
		System.out.println();

		System.out.println("I believe that you have played game Pokemon Go !!! This game is quite similar to that game but this time it is your heroes rather than your pokemon who will fight monsters.");
		System.out.println("In this game, you can fight monsters to earn money and experience, visit the market and participate in lucrative trades and explore unknown regions. ");
		System.out.println("If the hero loses during battle, they will be regenerated with some penalty!!");

		System.out.println("The money earned from battles can be used in the market to buy potions, weapons, armors and enticing spells which are helpful to win the next combat"	);
		System.out.println("You can also sell your goods in the market at half the price you bought it for");
		System.out.println("When you wander around on the map, you will encounter monsters. Stay strong and conquer!");
		System.out.println("Each hero will start from a fixed place or his own nexus. The aim is to reach the monster's nexus before they reach yours!");

		System.out.println();
		System.out.println("In the beginning you can choose 3 heroes to build your team");
		System.out.println("Good luck and above all remember to have fun!!!");

		heroMoving.get(0).connectMap(map,market,this);
		map.setHasRoleField();
		map.displayMonsterOrHero();

		while (true){
			oneRoleRound();
		}

	}


	public ArrayList<Hero> getHeroMoving() {
		return heroMoving;
	}

	public ArrayList<Hero> getHeroFighting() {
		return heroFighting;
	}

	public ArrayList<Monster> getMonsterAlive() {
		return monsterAlive;
	}

	private char oneRoleRound(){

		System.out.println(map);
		for (Hero hero : heroMoving) {
			hero.oneTurn();

//			updateMonsterHero();
//			System.out.println("Monster or Hero Map: ");
//			map.displayMonsterOrHero();
		}

		for(Monster mon : monsterAlive){
			Battle battleTemp = null;
			boolean laneBattleFlag = false;
			boolean shouldDoCombat = mon.oneTurn(heroFighting);
			if(shouldDoCombat && mon.getIsBattle() == false){
				for(Battle battleCurr : battles){
					if(battleCurr.getLane() == mon.laneOri){
						laneBattleFlag = true;
						battleTemp = battleCurr;
					}
				}
				//this means there was no battle ongoing in this lane at all so start a new battle
				if(laneBattleFlag == false) {
					int x = mon.getX();
					int y = mon.getY();
					ArrayList<Monster> currMonList = new ArrayList<>();
					ArrayList<Hero> currHeroList = new ArrayList<>();
					currMonList.add(mon);
					//find which is the hero or heroes in the monster's vicinity
					ArrayList<Hero> heroMovingCopy = new ArrayList<>();



					heroMovingCopy.addAll(heroMoving);

					for (Hero hero : heroMoving) {
						if (hero.getLaneCurr() == mon.getLaneCurr() && !hero.getHasWon() && !mon.getHasWon()) {
							if (hero.getX() + 1 == mon.getX() || hero.getX() - 1 == mon.getX() || hero.getX() == mon.getX()) {
								currHeroList.add(hero);
								hero.setIsBattle(true);
							}
						}
					}

					heroMovingCopy.removeAll(currHeroList);
					Battle battle = new Battle(currMonList, currHeroList, mon.laneOri,this);
					System.out.println("Battle added"+battle+" lane "+mon.laneCurr);
					battles.add(battle);


					// great coding  clear and maintain the reference
					heroMoving.clear();
					heroMoving.addAll(heroMovingCopy);
					heroFighting.addAll(currHeroList);

					heroMoving.removeAll(currHeroList);
				}
				//this means there is already an ongoing battle. then the monster is added to this battle
				else{
					ArrayList<Monster> currentMonsters = battleTemp.getMonsterList();
					if(!currentMonsters.contains(mon)){
						currentMonsters.add(mon);
						battleTemp.setMonsterList(currentMonsters);
					}
				}
			}


			//let hero fight here:

			System.out.println("Monster or Hero Map: ");

		}
		System.out.println(map);

		checkCreateNewMonsters();

		System.out.println("Battle size:" +battles.size());
		System.out.println("Battle detail:"+ battles);
		ArrayList<Battle> iteration = new ArrayList<>();

		iteration.addAll(battles);
		for (Battle ba : iteration) {
			ba.fightBattle();
		}
		regainHpHero();
		respawnHero();

		System.out.println("One round end for both hero and monster");
		roundsPlayed +=1;
		checkWinCondition();
		return 'H';
	}

	// if the game has been won or lost!
	private void checkWinCondition(){
		char lane1 = ' ';
		char lane2 = ' ';
		char lane3 = ' ';
		for (Monster mon: monsterAlive){
			if(mon.getHasWon() == true){
				if(mon.getLaneOri() == 1){
					lane1 = 'm';
				}
				else if(mon.getLaneOri() == 2){
					lane2 = 'm';
				}
				else lane3 = 'm';
			}
		}
		for (Hero h: heroMoving){
			if(h.getHasWon() == true){
				if(h.getLaneOri() == 1){
					lane1 = 'c';
				}
				else if(h.getLaneOri() == 2){
					lane2 = 'c';
				}
				else lane3 = 'c';
			}
		}
		if(lane1 == 'm' || lane2 == 'm' || lane3 =='m'){
			System.out.println(map);
			System.out.println("Oh no! The monsters have evaded your territory and captured the World of Play");
			System.out.println("We wish you better luck next time Brave Warriors! Come back soon and save this world!");
			System.exit(0);
		}
		else if(lane1 == 'c' || lane2 == 'c' || lane3 == 'c'){
			System.out.println(map);
			System.out.println("YOU WIN");
			System.out.println("Congratulations! The monsters have been defeated! You have brough peace back to the World of Play!");
			System.out.println("We wish you good luck on your next adventures! Come back soon and visit us! Till then Goodbye!");
			System.exit(0);

		}
	}
	//every 8 rounds new monster should be created
	private void checkCreateNewMonsters(){
		if(this.roundsPlayed %10 == 0){
			Monster mon = createNewMonster();
			mon.readyToDisplay(0,1);
			monsterAlive.add(mon);
			mon = createNewMonster();
			mon.readyToDisplay(0,4);
			monsterAlive.add(mon);
			mon = createNewMonster();
			mon.readyToDisplay(0,7);
			monsterAlive.add(mon);
		}
	}

	private Monster createNewMonster(){
		Monster m = null;
		Collections.shuffle(monList.getMonsterList());
		int i = 0;
		boolean flag = false;
		while(flag == false){
			if(this.monsterAlive.contains(monList.getMonsterList().get(i))){
				i += 1;
			}
			else flag = true;
			m = monList.getMonsterList().get(i);
		}
		try {
			return (Monster) m.clone();
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException(e);
		}
	}

	private int regainHpHero(){
		for (Hero alive : heroMoving) {
			alive.regainHpMana();
		}
		return 0;
	}

	public ArrayList<Battle> getBattles() {
		return battles;
	}

	public Map getMap() {
		return map;
	}

	private int respawnHero(){

		ArrayList<Hero> heroCorpsesBody = new ArrayList<>();

		heroCorpsesBody.addAll(heroCorpses);

		for (Hero corps : heroCorpses) {
			if(			corps.reSpawn()){
				heroCorpsesBody.remove(corps);
				heroMoving.add(corps);
				corps.setHp(corps.getMaxHP());
				corps.setWheFaint(false);
			}
		}// bad coding change the reference
//		heroCorpses = heroCorpsesBody;

		//GREAT maintain the reference not changing it.
		heroCorpses.clear();
		heroCorpses.addAll(heroCorpsesBody);

		return 0;
	}

	private Map updateMonsterHero(){

		System.out.println("Reset grid");
		System.out.println(map.getFINAL_GRID());

		return map;
	}


	private ArrayList<Role> setupRoles(){
		roles = new ArrayList<>();
		heroMoving = new ArrayList<>();
		heroCorpses = new ArrayList<>();
		monsterAlive = new ArrayList<>();
		Hero.connectHeroParty(heroMoving,heroCorpses);


		int chosen=2;

        while (heroMoving.size()<3){
			chosen*=2;
            System.out.println(heroList);
            chosen = Helper.getIntInput("Which hero do you want to choose?",heroList.getHeroList().size());
//
//			heroAlive.add((Hero) heroList.getHeroList().get(chosen));

			try {
				heroMoving.add((Hero) heroList.getHeroList().get(chosen).clone());
			} catch (CloneNotSupportedException e) {
				throw new RuntimeException(e);
			}

		}


//		Role hero =  heroList.getHeroList().get(2);
//		hero.readyToDisplay(0,1);
//		roles.add(hero);
//		heroAlive.add((Hero) hero);
//		hero =  heroList.getHeroList().get(4);
//		hero.readyToDisplay(7,3);
//		roles.add(hero);
//		heroAlive.add((Hero) hero);
//		hero =  heroList.getHeroList().get(16);
//		hero.readyToDisplay(7,6);
//		roles.add(hero);
//		heroAlive.add((Hero) hero);



//		hero.connectMap(map);


		Role monster = null;
		try {
			monster = (Role) createNewMonster().clone();
			monster.readyToDisplay(0,1);
			roles.add(monster);
			monsterAlive.add((Monster) monster);
			monster = (Role) createNewMonster().clone();
			monster.readyToDisplay(0,4);
			roles.add(monster);
			monsterAlive.add((Monster) monster);
			monster = (Role) createNewMonster().clone();
			monster.readyToDisplay(0,7);
			roles.add(monster);
			monsterAlive.add((Monster) monster);

		} catch (CloneNotSupportedException e) {
			throw new RuntimeException(e);
		}


		return roles;






	}

	
	// This function is in charge of the team movement
	public int updateLocation(char dir){
		String direction = String.valueOf(dir);

		// go upwards
		if (direction.equals("W") || direction.equals("w")) {
			//first to check whether the place is movable
//			if (map.getxCord() == 0  || map.grid[map.getxCord() - 1][map.getyCord()].getType() == 'X') {
//				System.out.println("The place you choose can not enter! Please try another place!");
//			} else {
//				 the case when step in a market
//					if (map.grid[map.getxCord() - 1][map.getyCord()].getType() == 'M') {
//					map.grid[map.getxCord()][map.getyCord()].setType(' ');
//					map.grid[map.getxCord() - 1][map.getyCord()].setType('T');
//						map.setxCord(map.getxCord() - 1);
//
//						for (Hero he: heroPP.getHeroParty()) {
//							market.enterMaket(he);
//						};
//				}//normally move here
//				else if (map.grid[map.getxCord() - 1][map.getyCord()].getType() == ' ') {
//					map.grid[map.getxCord()][map.getyCord()].setType(' ');
//					map.grid[map.getxCord() - 1][map.getyCord()].setType('T');
//					map.setxCord(map.getxCord() - 1);
//
//					call encounterMonster function to decide whether start a fight
//					if (encounterMonster()) {
//						combat.fight();
//						
//					}
//				}
//			}
		}

		// go leftwards
		else if (direction.equals("A") || direction.equals("a")) {
//			if (map.getyCord() == 0 || map.grid[map.getxCord()][map.getyCord() - 1].getType() == 'X') {
//				System.out.println("The place you choose can not enter! Please try another place!");
//			} else {
//					if (map.grid[map.getxCord()][map.getyCord() - 1].getType() == 'M') {
//					map.grid[map.getxCord()][map.getyCord()].setType(' ');
//					map.grid[map.getxCord()][map.getyCord() - 1].setType('T');
//					map.setyCord(map.getyCord()-1);
//						for (Hero he: heroPP.getHeroParty()) {
//						market.enterMaket(he);
//				};

//					}
//				else if (map.grid[map.getxCord()][map.getyCord() - 1].getType() == ' ') {
//					map.grid[map.getxCord()][map.getyCord()].setType(' ');
//					map.grid[map.getxCord()][map.getyCord() - 1].setType('T');
//					map.setyCord(map.getyCord() - 1);
//
//					if (encounterMonster()) {
//						combat.fight();
//						
//					}
//				}
//			}
		}

		// go downwards
		else if (direction.equals("S") || direction.equals("s")) {
//			if (map.getxCord() == (map.getSize() - 1) || map.grid[map.getxCord() + 1][map.getyCord()].getType() == 'X') {
//				System.out.println("The place you choose can not enter! Please try another place!");
//			} else {
//					if (map.grid[map.getxCord() + 1][map.getyCord()].getType() == 'M') {
//					map.grid[map.getxCord()][map.getyCord()].setType(' ');
//					map.grid[map.getxCord() + 1][map.getyCord()].setType('T');
//					map.setxCord(map.getxCord()+1);
//						for (Hero he: heroPP.getHeroParty()) {
//						market.enterMaket(he);
//				};
//
//
//					}
//				else if (map.grid[map.getxCord() + 1][map.getyCord()].getType() == ' ') {
//					map.grid[map.getxCord()][map.getyCord()].setType(' ');
//					map.grid[map.getxCord() + 1][map.getyCord()].setType('T');
//					map.setxCord(map.getxCord() + 1);
//
//					if (encounterMonster()) {
//						combat.fight();
//					}
//				}
//			}
		}

		// go rightwards
		else if (direction.equals("D") || direction.equals("d")) {
//			if (map.getyCord() == (map.getSize() - 1) || map.grid[map.getxCord()][map.getyCord() + 1].getType() == 'X') {
//				System.out.println("The place you choose can not enter! Please try another place!");
//			} else {
//				if (map.grid[map.getxCord()][map.getyCord() + 1].getType() == 'X') {
//					System.out.println("Inaccessible area! Please try another direction.");
//				}
//				else if (map.grid[map.getxCord()][map.getyCord() + 1].getType() == 'M') {
//					map.grid[map.getxCord()][map.getyCord()].setType(' ');
//					map.grid[map.getxCord()][map.getyCord() + 1].setType('T');
//					map.setyCord(map.getyCord() + 1);
//					for (Hero he: heroPP.getHeroParty()) {
//						market.enterMaket(he);
//				};
//
//				}else if (map.grid[map.getxCord()][map.getyCord() + 1].getType() == ' ') {
//					map.grid[map.getxCord()][map.getyCord()].setType(' ');
//					map.grid[map.getxCord()][map.getyCord() + 1].setType('T');
//					map.setyCord(map.getyCord() + 1);
//
//					if (encounterMonster()) {
//						combat.fight();
//					}
//				}
//			}
		}
//		map.printWorld();
		System.out.print(map);
		map.resetIdioticGrid();

		return 0;
	}
	
	
	// to randomly decide whether the hero has encounter a monster
	// Here you can change the probability to encounter a monster prob = threshold/bound
	//monprob
	private boolean encounterMonster(){
		int bound = 128;
		int r = random.nextInt(bound);
		int threshold = 96;
		if (r<threshold){
			return true;
			
		}else{
			return false;
		}
	}

}

