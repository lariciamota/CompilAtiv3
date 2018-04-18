package br.ufpe.cin.if688.visitor;

import br.ufpe.cin.if688.ast.AssignStm;
import br.ufpe.cin.if688.ast.CompoundStm;
import br.ufpe.cin.if688.ast.EseqExp;
import br.ufpe.cin.if688.ast.Exp;
import br.ufpe.cin.if688.ast.ExpList;
import br.ufpe.cin.if688.ast.IdExp;
import br.ufpe.cin.if688.ast.LastExpList;
import br.ufpe.cin.if688.ast.NumExp;
import br.ufpe.cin.if688.ast.OpExp;
import br.ufpe.cin.if688.ast.PairExpList;
import br.ufpe.cin.if688.ast.PrintStm;
import br.ufpe.cin.if688.ast.Stm;

public class MaxArgsVisitor implements IVisitor<Integer> {
/* Deve ser recebido um programa representado com os nós da AST disponíveis (ver exemplos na classe Prog) 
 * e deve ser retornado o número máximo de argumentos passados em algum comando print do programa; 
 * (apenas 1 valor)
 */
	@Override
	public Integer visit(Stm s) {
		return s.accept(this);
	}

	@Override
	public Integer visit(AssignStm s) {
		return s.getExp().accept(this);
	}

	@Override
	public Integer visit(CompoundStm s) {
		int esq = s.getStm1().accept(this);
		int dir = s.getStm2().accept(this);
		return Math.max(esq, dir);
	}
	
	public Integer count(ExpList e){
		int count = 1;
		if (e instanceof LastExpList){
			int last = ((LastExpList) e).accept(this);
			return Math.max(count, last);
		} else { //PairExpList
			int pair = ((PairExpList) e).accept(this);
			int count2 = count + count(((PairExpList) e).getTail());
			return Math.max(pair, count2);
		}
	}
	
	@Override
	public Integer visit(PrintStm s) {
		ExpList exps = s.getExps();
		
		return count(exps);
	}

	@Override
	public Integer visit(Exp e) {
		return e.accept(this); 
	}

	@Override
	public Integer visit(EseqExp e) {
		int stm = e.getStm().accept(this);
		int exp = e.getExp().accept(this);
		return Math.max(stm, exp); 
	}

	@Override
	public Integer visit(IdExp e) {
		return 0;
	}

	@Override
	public Integer visit(NumExp e) {
		return 0;
	}

	@Override
	public Integer visit(OpExp e) {
		int left = e.getLeft().accept(this);
		int right = e.getRight().accept(this);
		return Math.max(left, right); 
	}

	@Override
	public Integer visit(ExpList el) {
		return el.accept(this); 
	}

	@Override
	public Integer visit(PairExpList el) {
		int exp = el.getHead().accept(this);
		int list = el.getTail().accept(this); 
		return Math.max(exp, list); 
	}

	@Override
	public Integer visit(LastExpList el) {
		return el.getHead().accept(this); 
	}
	

}
