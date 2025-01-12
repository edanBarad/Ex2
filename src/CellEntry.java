public class CellEntry  implements Index2D {
    private int x, y;
    private String cords;

    public CellEntry(String cords){
        if (cords != null)
        {
            this.cords = cords.toUpperCase();
            insertEntry();
        } else
        {
            x = Ex2Utils.ERR;
            y = Ex2Utils.ERR;
            this.cords = Ex2Utils.EMPTY_CELL;
        }
    }

    public CellEntry (int xx, int yy) {
        this.x = xx;
        this.y = yy;
        this.cords = Ex2Utils.EMPTY_CELL;
        this.cords += (char)(xx+65);
        this.cords += Integer.toString(yy);
    }

    private void insertEntry(){
        if (this.cords.isEmpty() || this.cords.length() < 2){
            x = Ex2Utils.ERR;
            y = Ex2Utils.ERR;
            return;
        }
        char collum = Character.toUpperCase(this.cords.charAt(0));
        if (collum < 'A' || collum > 'Z'){
            x = Ex2Utils.ERR;
            y = Ex2Utils.ERR;
            return;
        }
        try{
            x = collum - 'A';
            y = Integer.parseInt(this.cords.substring(1));
            if (y < 0 || y > 99)
            {
                x = Ex2Utils.ERR;
                y = Ex2Utils.ERR;
            }
        }catch (NumberFormatException e)
        {
            x = Ex2Utils.ERR;
            y = Ex2Utils.ERR;
        }
    }

    @Override
    public boolean isValid() {
        if (this.x > 26 || this.y > 99) return false;
        return true;
    }

    @Override
    public int getX() {return Ex2Utils.ERR;}

    @Override
    public int getY() {return Ex2Utils.ERR;}

    @Override
    public String toString() {
        if (!isValid()) return Ex2Utils.EMPTY_CELL;
        else return this.cords;
    }
}