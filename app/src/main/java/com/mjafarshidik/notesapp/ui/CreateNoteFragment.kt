package com.mjafarshidik.notesapp.ui

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.mjafarshidik.notesapp.R
import com.mjafarshidik.notesapp.database.NotesDatabase
import com.mjafarshidik.notesapp.entities.Notes
import kotlinx.android.synthetic.main.fragment_create_note.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*


class CreateNoteFragment : Fragment() {

    var currentDate:String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_note, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            CreateNoteFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }

    @SuppressLint("SimpleDateFormat")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
        currentDate = sdf.format(Date())

        tvDateTime.text = currentDate

        imgDone.setOnClickListener {
            saveNote()
        }

        imgBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    private fun saveNote() {
        if (etNoteTitle.text.isNullOrEmpty()) {
            Toast.makeText(context, "Note Title is Required", Toast.LENGTH_SHORT).show()
        } else if (etNoteSubTitle.text.isNullOrEmpty()) {

            Toast.makeText(context, "Note Sub Title is Required", Toast.LENGTH_SHORT).show()
        } else if (etNoteDesc.text.isNullOrEmpty()) {

            Toast.makeText(context, "Note Description is Required", Toast.LENGTH_SHORT).show()
        } else {
            CoroutineScope(Dispatchers.Main).launch{
                var notes = Notes()
                notes.title = etNoteTitle.text.toString()
                notes.subTitle = etNoteSubTitle.text.toString()
                notes.noteText = etNoteDesc.text.toString()
                notes.dateTime = currentDate
//                notes.color = selectedColor
//                notes.imgPath = selectedImagePath
//                notes.webLink = webLink

                context?.let {
                    NotesDatabase.getDatabase(it).noteDao().insertNotes(notes)
                    etNoteTitle.setText("")
                    etNoteSubTitle.setText("")
                    etNoteDesc.setText("")
                    layoutImage.visibility = View.GONE
                    imgNote.visibility = View.GONE
                    tvWebLink.visibility = View.GONE
                    requireActivity().supportFragmentManager.popBackStack()
                }
            }
        }
    }
}