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
import br.ufpe.cin.if688.symboltable.IntAndTable;
import br.ufpe.cin.if688.symboltable.Table;

public class IntAndTableVisitor implements IVisitor<IntAndTable> {
	private Table t;

	public IntAndTableVisitor(Table t) {
		this.t = t;
	}

	@Override
	public IntAndTable visit(Stm s) {
		return s.accept(this);
	}

	@Override
	public IntAndTable visit(AssignStm s) {
		this.t.id = s.getId();
		this.t.value = s.getExp().accept(this).result;
		IntAndTable it = new IntAndTable(this.t.value, this.t);
		return it;
	}

	@Override
	public IntAndTable visit(CompoundStm s) {
		//?
		return null;
	}

	@Override
	public IntAndTable visit(PrintStm s) {
		return s.getExps().accept(this);
	}

	@Override
	public IntAndTable visit(Exp e) {
		return e.accept(this);
	}

	@Override
	public IntAndTable visit(EseqExp e) {
		Table tstm = e.getStm().accept(new Interpreter(this.t));
		this.t = tstm;
		IntAndTable itexp = e.getExp().accept(this);
		IntAndTable it = new IntAndTable(itexp.result, tstm);
		return it;
	}

	@Override
	public IntAndTable visit(IdExp e) {
		IntAndTable it = new IntAndTable(0, t);
		if (this.t.id == e.getId()){
			it.result = this.t.value;
		} else {
			it = new IntAndTableVisitor(t.tail).visit(e);
		}
		return it;
	}

	@Override
	public IntAndTable visit(NumExp e) {
		IntAndTable it = new IntAndTable(e.getNum(), t);
		return it;
	}

	@Override
	public IntAndTable visit(OpExp e) {
		int left = e.getLeft().accept(this).result;
		int right = e.getRight().accept(this).result;
		int op = e.getOper();
		int result = 0;
		switch(op) {
		case 1:
			result = left + right;
			break;
		case 2:
			result = left - right;
			break;
		case 3:
			result = left * right;
			break;
		case 4:
			result = left / right;
			break;
		default: System.out.println("Op invalida");
		}
		IntAndTable it = new IntAndTable(result, this.t);
		return it;
	}

	@Override
	public IntAndTable visit(ExpList el) {
		return el.accept(this);
	}

	@Override
	public IntAndTable visit(PairExpList el) {
		el.getHead().accept(this);
		el.getTail().accept(this);
		return null;
	}

	@Override
	public IntAndTable visit(LastExpList el) {
		return el.getHead().accept(this);
	}


}
