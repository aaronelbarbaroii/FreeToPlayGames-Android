package com.jjvision.freetoplaygames_android.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.jjvision.freetoplaygames_android.R
import com.jjvision.freetoplaygames_android.adapters.GalleryAdapter
import com.jjvision.freetoplaygames_android.data.Game
import com.jjvision.freetoplaygames_android.data.GameService
import com.jjvision.freetoplaygames_android.databinding.ActivityDetailBinding
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class DetailActivity : AppCompatActivity() {

    companion object{
        const val EXTRA_GAME_ID = "GAME_ID"
    }

    lateinit var binding: ActivityDetailBinding
    lateinit var game: Game

    var showMoreEnabled = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val id = intent.getIntExtra(DetailActivity.Companion.EXTRA_GAME_ID, -1)

        gameId(id)

    }

    fun loadData() {
        // Basic info
        supportActionBar?.title = game.title
        supportActionBar?.subtitle = game.developer
        binding.titleTextView.text = game.title
        binding.descriptionTextView.text = game.description
        binding.showMoreTextView.setOnClickListener {
            if(showMoreEnabled){
                binding.descriptionTextView.maxLines = 5
                binding.showMoreTextView.text = "Ver más..."
            }
            else {
                binding.descriptionTextView.maxLines = Int.MAX_VALUE
                binding.showMoreTextView.text = "Ver menos..."
            }
            showMoreEnabled = !showMoreEnabled
        }

        // Aditional info
        binding.genreChip.text = game.genre
        when(game.platform){
            "PC_Windows" -> binding.platformButton.setIconResource(R.drawable.ic_desktop_windows)
            "Web browser" -> binding.platformButton.setIconResource(R.drawable.ic_web)
        }

        // Video
        binding.trailerVideoView.setVideoURI("https://www.freetogame.com/g/${game.id}/videoplayback.webm".toUri())
        binding.trailerVideoView.setOnPreparedListener {
            CoroutineScope(Dispatchers.IO).launch {
                delay(3000)
                CoroutineScope(Dispatchers.Main).launch {
                    binding.thumbnailImageView.visibility = View.GONE
                    binding.trailerVideoView.start()
                }
            }
        }
        binding.trailerVideoView.setOnCompletionListener {
            binding.thumbnailImageView.visibility = View.VISIBLE
        }

        // Image
        Picasso.get().load(game.thumbnail).into(binding.thumbnailImageView)
        val adapter = GalleryAdapter(game.screenshots, -1) { position ->
            val intent = Intent(this, GalleryActivity::class.java)
            intent.putExtra(GalleryActivity.EXTRA_SCREENSHOT_INDEX, position)
            intent.putExtra(
                GalleryActivity.EXTRA_SCREENSHOTS_ARRAY,
                game.screenshots.map { it.image }.toTypedArray()
            )
            startActivity(intent)
        }
        binding.screenshotsRecyclerView.adapter = adapter
        binding.screenshotsRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        // Buttons
        binding.playButton.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, game.urlGame.toUri())
            startActivity(browserIntent)
        }
        binding.shareButton.setOnClickListener {
            val sendIntent = Intent()
            sendIntent.setAction(Intent.ACTION_SEND)
            sendIntent.putExtra(Intent.EXTRA_TEXT, "Mira que juego esta gratis: ${game.profileUrl}")
            sendIntent.setType("text/plain")

            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)
        }

        binding.osTextView.text = game.minimumSystemRequirements?.os ?: "-----"
        binding.processorTextView.text = game.minimumSystemRequirements?.processor ?: "-----"
        binding.memoryTextView.text = game.minimumSystemRequirements?.memory ?: "-----"
        binding.graphicsTextView.text = game.minimumSystemRequirements?.graphics ?: "-----"
        binding.storageTextView.text = game.minimumSystemRequirements?.storage ?: "-----"

    }

    fun gameId(id: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val service = GameService.getInstance()
                game = service.getGameById(id)
                CoroutineScope(Dispatchers.Main).launch {
                    loadData()
                }
            } catch (e: Exception){
                e.printStackTrace()
            }
        }
    }

}