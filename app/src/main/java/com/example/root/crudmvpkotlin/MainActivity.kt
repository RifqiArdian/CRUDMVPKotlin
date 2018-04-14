package com.example.root.crudmvpkotlin

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.PopupMenu
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.example.root.crudmvpkotlin.model.Todo
import com.example.root.crudmvpkotlin.presenter.TodoMvp
import com.example.root.crudmvpkotlin.presenter.TodoPresenter
import com.example.root.crudmvpkotlin.view.DialogTodo
import java.util.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_list.view.*


class MainActivity : AppCompatActivity(), TodoMvp.TodoView, SwipeRefreshLayout.OnRefreshListener {


    var adapter: RvAdapter = RvAdapter(ArrayList<Todo>())
    var presenter: TodoPresenter? = null
    var dialog:DialogTodo? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        rvMain.layoutManager = LinearLayoutManager(this)
        rvMain.adapter = adapter

        val app: TodoApplication = this.application as TodoApplication
        presenter = TodoPresenter(this, app.db)
        dialog = DialogTodo(this, presenter!!)

        presenter?.getAllData()


        fabAdd.setOnClickListener {
            dialog?.clear()
            dialog?.showDialog(false, null)
        }
    }

    override fun setData(listTodos: List<Todo>) {
        txtEmpty.visibility = View.GONE
        adapter.update(listTodos as MutableList<Todo>)
    }

    override fun setEmpty() {
        txtEmpty.visibility = View.VISIBLE
    }

    override fun setResult(message: String) {
        Snackbar.make(layRoot, message, Snackbar.LENGTH_SHORT).show()
    }

    override fun onRefresh() {
        presenter!!.getAllData()
    }

    override fun onLoad(isLoad: Boolean) {
        refresh.isRefreshing = isLoad
    }


    inner class RvAdapter(lsTodos: MutableList<Todo>) : RecyclerView.Adapter<ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view: View = LayoutInflater.from(parent?.context).inflate(R.layout.item_list, null, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {

            holder?.bindValue(lsTodos[position])

            holder?.itemView?.vOption!!.setOnClickListener {
                val popUp: PopupMenu = PopupMenu(holder?.itemView!!.context, holder.itemView.vOption)
                popUp.inflate(R.menu.menu_more)
                val menuOption: PopupMenu.OnMenuItemClickListener = PopupMenu.OnMenuItemClickListener {
                    menuItem:MenuItem ->
                    if(menuItem.itemId == R.id.menuDelete){
                        this@MainActivity.presenter?.deleteData(lsTodos[position].mId)
                        true
                    }else{
                        //MainActivity().presenter.updateData(lsTodos[position].mId)
                        this@MainActivity.dialog?.showDialog(true, lsTodos[position])
                        true
                    }
                }

                popUp.setOnMenuItemClickListener(menuOption)
                popUp.show()

            }
        }

        var lsTodos: MutableList<Todo> = lsTodos

        override fun getItemCount() = lsTodos.size

        fun update(lsTodos: MutableList<Todo>){
            this.lsTodos = lsTodos
            notifyDataSetChanged()
        }

    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindValue(todo: Todo) {

            with(todo){
                itemView.txtTitle.text = "$mTitle"
                itemView.txtDesc.text = "$mDesc"
                itemView.txtDate.text = "$mDate"
            }
        }

    }
}

