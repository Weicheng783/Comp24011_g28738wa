import java.util.ArrayList; 
// import java.util.List; 
import java.util.Arrays; 

public class MoveChooser {
  
    public static Move chooseMove(BoardState boardState){

	int searchDepth= Othello.searchDepth;
	

        ArrayList<Move> moves= boardState.getLegalMoves();
        if(moves.isEmpty()){
            return null;
		}
		// for(int i =0; i< moves.size(); i++){
		// 	System.out.print(" x:"+moves.get(i).x+" y:"+moves.get(i).y);
		// }
		// System.out.print("\n");
        // return moves.get(0);
        int[] ress = minimax(moves,searchDepth,boardState);
        if(ress.length != 0){
        	return moves.get(ress[3]);
        }else{
        	return null;
        }
    }

    public static int[] minimax(ArrayList<Move> moveList, int depth, BoardState bs){
    	int[][] staticEvaluationFunction = {{120,-20,20,5,5,20,-20,120},{-20,-40,-5,-5,-5,-5,-40,-20},{20,-5,15,3,3,15,-5,20},{5,-5,3,3,3,3,-5,5},{5,-5,3,3,3,3,-5,5},{20,-5,15,3,3,15,-5,20},{-20,-40,-5,-5,-5,-5,-40,-20},{120,-20,20,5,5,20,-20,120}};
    	if(depth == 0){
    		if(moveList != null || moveList.size() != 0){
	    		System.out.println(moveList);
	    		if(moveList == null){
	    			return null;
	    		}
	    		try{
		    		if(bs.colour == 1){
		    			int tempval = staticEvaluationFunction[moveList.get(0).x][moveList.get(0).y];
		    			int[] init = {staticEvaluationFunction[moveList.get(0).x][moveList.get(0).y],moveList.get(0).x,moveList.get(0).y,0};
		    			for(int i=1; i<moveList.size(); i++){
		    				if(staticEvaluationFunction[moveList.get(i).x][moveList.get(i).y] > tempval){
		    					tempval = staticEvaluationFunction[moveList.get(i).x][moveList.get(i).y];
		    					init = new int[] {staticEvaluationFunction[moveList.get(i).x][moveList.get(i).y],moveList.get(i).x,moveList.get(i).y,i};
		    				}
		    			}
		    			return init;

		    		}else{
		    			int tempval = staticEvaluationFunction[moveList.get(0).x][moveList.get(0).y];
		    			int[] init = {staticEvaluationFunction[moveList.get(0).x][moveList.get(0).y],moveList.get(0).x,moveList.get(0).y,0};
		    			for(int i=1; i<moveList.size(); i++){
		    				if(staticEvaluationFunction[moveList.get(i).x][moveList.get(i).y] < tempval){
		    					tempval = staticEvaluationFunction[moveList.get(i).x][moveList.get(i).y];
		    					init = new int[] {staticEvaluationFunction[moveList.get(i).x][moveList.get(i).y],moveList.get(i).x,moveList.get(i).y,i};
		    				}
		    			}
		    			return init;	    			
		    		}
		    	}catch(Exception e){
		    		return null;

		    	}
    		}else{
    			return null;
    		}
    	}
    	// int[][] candidates = new int[moveList.size()][3];
    	int[][] candidates = new int[0][4];
    	// ArrayList<Move> moves= bs.getLegalMoves();
    	// int indicator = 0;
		for(int i=0; i<moveList.size(); i++){
			BoardState bss = bs.deepCopy();
			bss.makeLegalMove(moveList.get(i).x,moveList.get(i).y);
			ArrayList<Move> ml = bss.getLegalMoves();
			if(ml == null || ml.size() == 0){
				// continue;
				candidates = Arrays.copyOf(candidates,candidates.length+1);
				int[] res = {staticEvaluationFunction[moveList.get(i).x][moveList.get(i).y],moveList.get(i).x,moveList.get(i).y,i};
				candidates[i] = res;
				// indicator ++;
			}else{
				int[] res = minimax(ml, depth-1, bss);
				// if(res != null){
					candidates = Arrays.copyOf(candidates,candidates.length+1);
					// new int[candidates.length+1][3];
					

					// newcandid[i] = res;
					candidates[i] = res;
					// indicator ++;
				// }				
			}

			
		}
		System.out.println(candidates.length);

    	if(bs.colour == 1){
    		if(candidates.length != 0){
	    		int max = candidates[0][0];
	    		candidates[0][3] = 0;
	    		int[] maxarray = candidates[0];

	    		for(int i=0; i<candidates.length; i++){
	    			if(i != 0 && candidates[i][0]>max){
	    				max = candidates[i][0];
	    				candidates[i][3] = i;
	    				maxarray = candidates[i];
	    			}
	    		}
	    		return maxarray;    			
    		}else{
    			return null;
    		}
    		// Arrays.sort(candidates.toArray());
    		// return candidates[candidates.length-1];
    		// return candidates[0];
    	}else{
    		// Arrays.sort(candidates);
    		// return candidates[0];
    		// return Math.min(candidates);
    		if(candidates.length != 0){
	    		int min = candidates[0][0];
	    		candidates[0][3] = 0;
	    		int[] minarray = candidates[0];
	    		for(int i=0; i<candidates.length; i++){
	    			if(i != 0 && candidates[i][0]<min){
	    				min = candidates[i][0];
	    				candidates[i][3] = i;
	    				minarray = candidates[i];
	    			}
	    		}

	    		return minarray;
    		}else{
    			return null;
    		}
    	}
    }

    // public Move minimax(){

    // }
}
