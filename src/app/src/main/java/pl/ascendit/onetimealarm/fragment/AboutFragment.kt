/*
 * Copyright (c) 2022 Ascendit Sp. z o. o.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see https://www.gnu.org/licenses/
 *
 */

package pl.ascendit.onetimealarm.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import pl.ascendit.onetimealarm.R
import pl.ascendit.onetimealarm.databinding.FragmentAboutBinding


class AboutFragment : Fragment() {
    private lateinit var binding: FragmentAboutBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentAboutBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mailSubject = getString(R.string.mail_subject)
        val mailAddress = getString(R.string.mail_address)
        val websiteUrl = getString(R.string.website_url)
        val githubUrl = getString(R.string.github_url)
        val linkedinUrl = getString(R.string.linkedin_url)

        binding.ivMail.setOnClickListener {
            sendMail(mailAddress, mailSubject)
        }

        binding.ivLinkedin.setOnClickListener {
            startBrowser(linkedinUrl)
        }

        binding.ivGithub.setOnClickListener {
            startBrowser(githubUrl)
        }

        binding.ivWebsite.setOnClickListener {
            startBrowser(websiteUrl)
        }
    }

    fun startBrowser(url: String) {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
    }

    fun sendMail(email: String, subject: String) {
        val emailIntent = Intent(Intent.ACTION_SENDTO)
        emailIntent.setData(Uri.parse("mailto:${email}"));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        startActivity(Intent.createChooser(emailIntent, "Send email"))
    }
}
