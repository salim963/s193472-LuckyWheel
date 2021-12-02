package com.example.s193472_lucywheel

import android.content.Context
import android.media.MediaPlayer
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.DecelerateInterpolator
import android.view.animation.RotateAnimation
import android.widget.*
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.s193472_luckywheel.Adapter.MyAdapter
import com.example.s193472_lucywheel.Data.AnimalsName
import com.example.s193472_lucywheel.Data.MyMemory
import java.util.*

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class PlayFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    private val sectors = arrayOf(
        "100", "Bankrupt", " 2500", "Ekstra turn", "300", "500", "Miss turn"
    )


    /** We create a Random instance **/
    private val RANDOM = Random()
    private var degree = 0
    private var lastdegree = 0
    private val HALF_SECTOR = 360f / 7f / 2f

    /** TextView lives**/
    lateinit var live: TextView
    lateinit var pointantal : TextView

    /** antal live at all **/
    var lives = 5
    var points = 0

    //var spineAndMinus = true
    var spinTheWheel = true


    lateinit var wheel: ImageView
    lateinit var resultwheel: TextView

    lateinit var guessbox: EditText
    lateinit var spin: Button

    lateinit var enterletter: Button
    lateinit var randomWordDis: String
    lateinit var randomWord: String
    val MyAnimalsName = MyMemory().loadAnimalsWords()
    var AnimalsNameDashes = mutableListOf<AnimalsName>()
    lateinit var MyRecyclerView: RecyclerView









    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_play, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Toast.makeText(context, "Spin the wheel and guess a letter", Toast.LENGTH_SHORT).show()
        guessbox = view.findViewById(R.id.Guessletter)
        spin = view.findViewById(R.id.spin)
        wheel = view.findViewById(R.id.pic)
        enterletter = view.findViewById(R.id.enterletter)
        resultwheel = view.findViewById(R.id.resultwheel)
        live = view.findViewById(R.id.live)
        pointantal = view.findViewById(R.id.pointantal)

        randomWord = MyAnimalsName.random().toString()
        randomWordDis = randomWord
        var randomWordletter = randomWord.toCharArray()

        for (i in randomWordletter) {
            if (i == ' ') {

                AnimalsNameDashes.add(AnimalsName(' ', true))
            } else {
                AnimalsNameDashes.add(AnimalsName('_', false))
            }
        }
        MyRecyclerView = view.findViewById(R.id.MyRecyclerView)
        MyRecyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        MyRecyclerView.setHasFixedSize(true)
        MyRecyclerView.adapter = MyAdapter(AnimalsNameDashes)




        spineWheel()
        enterletter.setOnClickListener {
            GuessLetter()
        }



    }
    fun GuessLetter() {



        val guess = guessbox.text.trim()
        if (guess.isEmpty()) {
            return
        }
        var guessedletter = guess.get(0)
        enterletter.isEnabled = false

        if (!randomWordDis.contains(guessedletter, true)) {
            // spin again
            lives--
            live.text.toString()
            live.setText(lives.toString())

            if (lives === 0) {
                enterletter.isEnabled = false
                Navigation.findNavController(requireView())
                    .navigate(R.id.action_playFragment_to_lostFragment)
            }

           return
        }
        var randomwordsletter = randomWord.toCharArray()
        for ((i, item) in randomwordsletter.withIndex()) {
            var word = AnimalsNameDashes.get(i)

            if (item === guessedletter && word.Guessed) {
                Toast.makeText(context, "the letter is found: " + guessedletter, Toast.LENGTH_SHORT)
                    .show()
                continue
            }
            if (item === guessedletter) {
                word.char = item
                word.Guessed = true
                Toast.makeText(context, "The letter was true", Toast.LENGTH_SHORT).show()
               // AddPoints()
            }

        }
        ////update view with new guess chars
        MyRecyclerView.adapter = MyAdapter(AnimalsNameDashes)

        //check if the whole random word is guessed
        var ifstillletters = AnimalsNameDashes.any { !it.Guessed }
        if (!ifstillletters) {

            //user guessed the whole word correctly
            enterletter.isEnabled = false
            Toast.makeText(context, "You make it !!!", Toast.LENGTH_LONG).show()
            Navigation.findNavController(requireView())
                .navigate(R.id.action_playFragment_to_wonFragment)


        }
    }



    fun spineWheel() {


        spin.setOnClickListener() {


            /*** Move to GameLostFragment****/


            /*if (lives == -1) {
                  //val tryagain = findViewById<Button>(R.id.tryagainlose)
                  supportFragmentManager.beginTransaction()
                      .add(R.id.maincontener, GameLostFragment())
                      .commit()
                  setVisible(true)
                  // val dialog = GameLostFragment()
                  //dialog.show(supportFragmentManager,"java.lang.Exception")
             }*/


            /** Animation for wheel*/
            lastdegree = degree % 360
            /**we calculate random angle for rotation of our wheel **/
            degree = RANDOM.nextInt(360) + 1300
            /** rotation effect on the center of the wheel **/
            val rotateAnim = RotateAnimation(
                lastdegree.toFloat(), degree.toFloat(),
                RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f
            )
            rotateAnim.duration = 7300
            rotateAnim.fillAfter = true
            rotateAnim.interpolator = DecelerateInterpolator()
            rotateAnim.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation) {
                    /** voice start **/
                    MediaPlayer()
                    enterletter.isEnabled = false
                }

                override fun onAnimationEnd(animation: Animation) {
                    enterletter.isEnabled = true
                    // we display the correct sector pointed by the triangle at the end of the rotate animation
                    resultwheel.text = getSector(360 - degree % 360)
                    print(getSector(360 - degree % 360))


                    when (resultwheel.text.toString()) {
                        "Bankrupt" -> {
                            Toast.makeText(
                                context,
                                "You went bankrupt, your points now is 0",
                                Toast.LENGTH_SHORT
                            ).show()
                            // point =0

                        }
                        "Ekstra turn" -> {
                            Toast.makeText(
                                context,
                                "You get ekstra turn",
                                Toast.LENGTH_SHORT
                            ).show()
                            lives += 1
                            live.text.toString()
                            live.setText(lives.toString())

                        }
                        "Miss turn" -> {
                            Toast.makeText(
                                context, "You lost an attempt", Toast.LENGTH_SHORT).show()
                            lives -= 1
                            live.text.toString()
                            live.setText(lives.toString())
                        }
                        "100" -> {
                            Toast.makeText(context, "You get 100", Toast.LENGTH_SHORT).show()
                            // ADDpoint
                        }
                        "300" -> {
                            Toast.makeText(context, "You get 300", Toast.LENGTH_SHORT).show()
                            // ADDpoint

                        }
                        "500" -> {
                            Toast.makeText(context, "You get 500", Toast.LENGTH_SHORT).show()
                            // ADDpoint

                        }
                        "2500" -> {
                            Toast.makeText(context, "You get 2500", Toast.LENGTH_SHORT).show()
                            // ADDpoint

                        }

                    }
                }
                override fun onAnimationRepeat(animation: Animation) {}
            })
            /** start the animation **/
            wheel.startAnimation(rotateAnim)
        }
    }

    /**fun to get value from sectors**/
    fun getSector(degrees: Int): String? {
        var i = 0
        var textfromwheel: String? = null
        do {
            // start and end of each sector on the wheel
            val start: Float = HALF_SECTOR * (i * 2 + 1)
            val end: Float = HALF_SECTOR * (i * 2 + 3)
            if (degrees >= start && degrees <= end) {

                // so text is equals to sectors[i];
                textfromwheel = sectors.get(i)
            }
            i++
            // now we can test our Android Roulette Game 🙂
            // That's all !

        } while (textfromwheel == null && i < sectors.size)

        return textfromwheel


    }
    /** voice fun **/
    fun MediaPlayer() {
        var mp: MediaPlayer = MediaPlayer.create(activity, R.raw.voice1)
        mp.setVolume(1.5f, 0.5f)
        mp.isLooping = false
        if (mp.isLooping) {
            mp.stop()
        } else {
            mp.start()
        }
    }
    fun AddPoints(){
        var TheNewPoints = resultwheel.text.toString()
        var FinslPoints = TheNewPoints + pointantal

        pointantal.text.toString()
        pointantal.setText(FinslPoints)


    }






}