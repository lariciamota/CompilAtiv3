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
import br.ufpe.cin.if688.visitor.IntAndTableVisitor;

public class Interpreter implements IVisitor<Table> {
	/*
	 * você deve "interpretar" o código usando Java,
	 * utilizando as classes Table e IntAndTable 
	 * como tabelas de símbolos auxiliares;
	 */
	//a=8;b=80;a=7;
	// a->7 ==> b->80 ==> a->8 ==> NIL
	private Table t;
	
	public Interpreter(Table t) {
		this.t = t;
	}

	@Override
	public Table visit(Stm s) {
		this.t = s.accept(this);
		return t;
	}

	@Override
	public Table visit(AssignStm s) {
		IntAndTable it = s.getExp().accept(new IntAndTableVisitor(t));
		this.t = new Table(s.getId(), it.result, it.table);
		return t;
	}

	@Override
	public Table visit(CompoundStm s) {
		// a = 512+3; print(a)
		//AssignStm --> resultado vai produzir uma tabela { a ==> 515 }
		s.getStm1().accept(this);
		s.getStm2().accept(this);
		return t;
	}
	
	public Table printaux(Exp e){
		IntAndTable it = e.accept(new IntAndTableVisitor(t));
		this.t = it.table;
		System.out.println(it.result);
		return t;
	}
	
	public void aux(ExpList el){
		if(el instanceof LastExpList){
			LastExpList le = (LastExpList) el;
			Exp e = le.getHead();
			printaux(e);
		} else {
			PairExpList pe = (PairExpList) el;
			Exp e = pe.getHead();
			printaux(e);
			aux(pe.getTail());
		}
	}

	@Override
	public Table visit(PrintStm s) {
		ExpList el = s.getExps();
		aux(el);
		return t;
	}

	@Override
	public Table visit(Exp e) {
		this.t = e.accept(this);
		return t;
	}

	@Override
	public Table visit(EseqExp e) {
		e.getStm().accept(this);
		e.getExp().accept(this);
		return t;
	}

	@Override
	public Table visit(IdExp e) {
		this.t.id = e.getId();
		return t;
	}

	@Override
	public Table visit(NumExp e) {
		this.t.value = e.getNum();
		return t;
	}

	@Override
	public Table visit(OpExp e) {
		IntAndTable it = e.accept(new IntAndTableVisitor(t));
		t.value = it.result;
		return t;
	}

	@Override
	public Table visit(ExpList el) {
		this.t = el.accept(this);
		return t;
	}

	@Override
	public Table visit(PairExpList el) {
		el.getHead().accept(this);
		el.getTail().accept(this);
		return t;
	}

	@Override
	public Table visit(LastExpList el) {
		el.getHead().accept(this);
		return t;
	}


}
