package ir.roela.countersaver.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import ir.roela.countersaver.G
import ir.roela.countersaver.R
import ir.roela.countersaver.model.Counter

class HomeFragment : Fragment(), View.OnClickListener {

    private var textView: TextView? = null
    private var btnCounterSave: ImageButton? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        textView = root.findViewById(R.id.text_home)
        val btnCounterPlus: ImageButton = root.findViewById(R.id.btnInCreaseCounter)
        val btnCounterMinus: ImageButton = root.findViewById(R.id.btnDeCreaseCounter)
        btnCounterSave = root.findViewById(R.id.btnSaveCounter)
        btnCounterPlus.setOnClickListener(this)
        btnCounterMinus.setOnClickListener(this)
        btnCounterSave?.setOnClickListener(this)
        if (G.counter == 0L) {
            btnCounterSave?.isEnabled = false
        }
        textView?.text = G.counter.toString()
        return root
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnInCreaseCounter -> increaseCount()
            R.id.btnSaveCounter -> showDialogSave()
            R.id.btnDeCreaseCounter -> decrementCounter()
        }
    }

    private fun increaseCount() {
        G.counter++
        textView?.text = G.counter.toString()
        btnCounterSave!!.isEnabled = true
        YoYo.with(Techniques.Landing)
            .duration(750)
            .playOn(textView)
    }

    private fun decrementCounter() {
        if (G.counter != 0L) {
            G.counter--
            textView?.text = G.counter.toString()
            YoYo.with(Techniques.Landing)
                .duration(750)
                .playOn(textView)
        }
    }

    private fun saveCount(name: CharSequence) {
        try {
            val counter = Counter()
            counter.countName = name.toString()
            counter.countValue = G.counter.toString()
            G.db!!.counterDao()!!.insert(counter)
            G.counter = 0
            btnCounterSave?.isEnabled = false
            Toast.makeText(context, "saved", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Log.e("counter", e.message.toString())
        }
    }

    private fun showDialogSave() {
        if (G.counter != 0L) {
            val saveDialog = AlertDialog.Builder(requireActivity()).create()
            val dlgView = LayoutInflater.from(context).inflate(R.layout.dlg_save_counter, null)
            val edtCountName = dlgView.findViewById<EditText>(R.id.edtCountName)
            val btnCancelDlg = dlgView.findViewById<Button>(R.id.btnCancelDlg)
            val btnDlgSaveCount = dlgView.findViewById<Button>(R.id.btnDlgSaveCount)
            btnCancelDlg.setOnClickListener {
                saveDialog.dismiss()
            }
            btnDlgSaveCount.setOnClickListener {
                val name = edtCountName.text.trim()
                if (name.length > 2) {
                    saveCount(name)
                    saveDialog.dismiss()
                } else {
                    Toast.makeText(context, "entered name very short!", Toast.LENGTH_SHORT).show()
                }
            }
            saveDialog.setView(dlgView)
            saveDialog.show()
        } else {
            Toast.makeText(context, "please Increase Counter", Toast.LENGTH_SHORT).show()
        }
    }

}