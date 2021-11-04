import java.util.ArrayList;
import java.util.Arrays;

public class MoveChooser {
    public static Move chooseMove(BoardState boardState){
		int searchDepth= Othello.searchDepth;
        ArrayList<Move> moves= boardState.getLegalMoves();
        if(moves.isEmpty()){
            return null;
		}
        // return moves.get(0);
        // We comment out the original method and put a very small or very large value in alpha and beta respectively.
        // We then store the choice in the ress.
        int[] ress = minimax(moves,searchDepth,boardState,-99999999,99999999);
        if(ress.length != 0){
        	return moves.get(ress[3]);
        }else{
        	return null;
        }
    }

    /*
    * This is the Static Evaluation Function,
    * we use the initial values to assign each square a weight.
	* And finally we return the value of the total white weight - total black weight.
    */
    public static int staticEvaluation(BoardState bs, int x, int y){
    	int[][] staticEvaluationMapping = {{120,-20,20,5,5,20,-20,120},{-20,-40,-5,-5,-5,-5,-40,-20},{20,-5,15,3,3,15,-5,20},{5,-5,3,3,3,3,-5,5},{5,-5,3,3,3,3,-5,5},{20,-5,15,3,3,15,-5,20},{-20,-40,-5,-5,-5,-5,-40,-20},{120,-20,20,5,5,20,-20,120}};
        int white= 0;
        int black= 0;
        for(int i= 0; i < 8; i++)
            for(int j=0; j < 8; j++)
                if(bs.getContents(i,j)==1)
                    white += staticEvaluationMapping[i][j];
                else if(bs.getContents(i,j)==-1)
                    black += staticEvaluationMapping[i][j];
        if(bs.colour == 1){
        	white += staticEvaluationMapping[x][y];
        }else{
        	black += staticEvaluationMapping[x][y];
        }
        int result = white - black;
        return result;
    }

    /*  
	* This is the main minimax algorithm with alpha-beta pruning.
	* The base case is when the depth = 0.
	* For each step case we iterate its children,
	* and finally we return an array of integers, the order is as follows:
	* [Node_Value, movement_x_value, movement_y_value, Node_Index, alpha/beta_returned_value].
    */
    public static int[] minimax(ArrayList<Move> moveList, int depth, BoardState bs, int alpha, int beta){
    	// If depth is 0, we return the static value of Node,
    	// if the Node has no child, we return null instead. 
    	if(depth == 0){
    		if(moveList != null || moveList.size() != 0){
	    		if(moveList == null){
	    			return null;
	    		}
	    		try{
	    			// At a maximizing node(white's turn), we change and return the alpha value as follows:
	    			// The value of Beta comes from the parent, Alpha value is the value of the highest-value child so far.
	    			// We stop processing the current node whenever alpha >= beta.
		    		if(bs.colour == 1){
		    			int tempval = staticEvaluation(bs,moveList.get(0).x,moveList.get(0).y);
		    			int[] init = {staticEvaluation(bs,moveList.get(0).x,moveList.get(0).y),moveList.get(0).x,moveList.get(0).y,0,alpha};
		    			for(int i=1; i<moveList.size(); i++){
		    				if(alpha < beta){
			    				if(staticEvaluation(bs,moveList.get(i).x,moveList.get(i).y) > tempval){
			    					tempval = staticEvaluation(bs,moveList.get(i).x,moveList.get(i).y);
			    					alpha = Math.max(alpha,tempval);
			    					init = new int[] {staticEvaluation(bs,moveList.get(i).x,moveList.get(i).y),moveList.get(i).x,moveList.get(i).y,i,alpha};
			    				}
		    				}else{
		    					break;
		    				}
		    			}
		    			return init;
		    		}else{
		    			// At a minimizing node(black's turn), we change and return the beta value as follows:
		    			// The value of Alpha comes from the parent, Beta value is the value of the lowest-value child so far.
		    			// We stop processing the current node whenever alpha >= beta.
		    			int tempval = staticEvaluation(bs,moveList.get(0).x,moveList.get(0).y);
		    			int[] init = {staticEvaluation(bs,moveList.get(0).x,moveList.get(0).y),moveList.get(0).x,moveList.get(0).y,0,beta};
		    			for(int i=1; i<moveList.size(); i++){
		    				if(alpha < beta){
			    				if(staticEvaluation(bs,moveList.get(i).x,moveList.get(i).y) < tempval){
			    					tempval = staticEvaluation(bs,moveList.get(i).x,moveList.get(i).y);
			    					beta = Math.min(beta,tempval);
			    					init = new int[] {staticEvaluation(bs,moveList.get(i).x,moveList.get(i).y),moveList.get(i).x,moveList.get(i).y,i,beta};
			    				}
		    				}else{
		    					break;
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

    	// If depth > 0, we search each of the children until alpha >= beta.
    	// The return value is an int array contains 5 values.
    	// We store the possible result in a 2d int array called candidates, it gathers all possible movements.
    	// From candidates we select the min/max element.
    	int[][] candidates = new int[0][5];
		for(int i=0; i<moveList.size(); i++){
			// We compare alpha and beta values here as this is the loop starting point.
			// It can detect and stop the processing whenever alpha >= beta.
			if(alpha < beta){
				// We copy the BoardState to bss, and simulate moves by using bss. 
				BoardState bss = bs.deepCopy();
				bss.makeLegalMove(moveList.get(i).x,moveList.get(i).y);
				ArrayList<Move> ml = bss.getLegalMoves();
				// If the moveList is null, we return the last node value found.
				if(ml == null || ml.size() == 0){
					// Enlarge array by one.
					candidates = Arrays.copyOf(candidates,candidates.length+1);
					// Return int array based on at minimizing/maximizing node.
					if(bs.colour == 1){
						int[] res = {staticEvaluation(bs,moveList.get(i).x,moveList.get(i).y),moveList.get(i).x,moveList.get(i).y,i,alpha};
						candidates[i] = res;
					}else{
						int[] res = {staticEvaluation(bs,moveList.get(i).x,moveList.get(i).y),moveList.get(i).x,moveList.get(i).y,i,beta};
						candidates[i] = res;
					}
				}else{
					// If the moveList is not null, we process each node by calling the minimax function iteratively.
					// And store the result in an int array called res. 
					int[] res = minimax(ml, depth-1, bss, alpha, beta);
					if(res != null){
						candidates = Arrays.copyOf(candidates,candidates.length+1);
						// Return int array based on at minimizing/maximizing node.
						// We update its alpha/beta value based on the node type we are in.
						if(bs.colour == 1){
							// Since res[4] represents the returned alpha/beta value,
							// we compare this value with the alpha/beta according to its node type.
							// And then update it if possible.
							alpha = Math.max(alpha, res[4]);
							res[4] = alpha;
						}else{
							beta = Math.min(beta, res[4]);
							res[4] = beta;
						}
						candidates[i] = res;
					}				
				}
			}else{
				break;
			}		
		}

		// We use a separate code snippet to return the minimum or maximum value.
		// This is for maximizing node, we return the max value of the movements pool.
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
    	}else{
    		// This is for the minimizing node(black's move), returns the minimize value from the movements pool.
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
}