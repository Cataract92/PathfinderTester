package Levels;

public class Level1 extends Level {

    public Level1(int size) {
        super(size);

        Field[][] map = getMap();

        for (int i = 0;i<20;i++)
        {
            map[10][i].status = -1;
        }

        map[10][3].status = 0;

        for (int i = 0;i<=15;i++)
        {
            map[8][i].status = -1;
        }
        for (int i = 0;i<=5;i++)
        {
            map[8-i][13].status = -1;
        }
        for (int i = 0;i<=6;i++)
        {
            map[6-i][5].status = -1;
        }
    }
}
