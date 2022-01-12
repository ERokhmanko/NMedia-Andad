package ru.netology.nmedia.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.R
import ru.netology.nmedia.auth.AppAuth
import ru.netology.nmedia.databinding.SignInFragmentBinding
import ru.netology.nmedia.di.DependencyContainer
import ru.netology.nmedia.viewmodel.SignInViewModel
import ru.netology.nmedia.viewmodel.SignViewModelFactory
import ru.netology.nmedia.viewmodel.ViewModelFactory

class SignInFragment : Fragment() {
    private val dependencyContainer = DependencyContainer.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = SignInFragmentBinding.inflate(inflater, container, false)

        val viewModel: SignInViewModel by viewModels(
            factoryProducer = {
                SignViewModelFactory(
                    dependencyContainer.repositoryAuth
                )
            })

        viewModel.data.observe(viewLifecycleOwner, {
            dependencyContainer.appAuth.setAuth(it.id, it.token)
            findNavController().navigateUp()
        })

        viewModel.dataState.observe(viewLifecycleOwner) { state ->
            if (state.errorLogin) {
                binding.passwordField.error = getString(R.string.error_auth)
            }
        }

        binding.signInButton.setOnClickListener {
            viewModel.authUser(
                binding.loginField.editText?.text.toString(),
                binding.passwordField.editText?.text.toString()
            )
        }

        binding.transitionSignUp.setOnClickListener {
            findNavController().navigate(R.id.action_signInFragment_to_signUpFragment)
        }

        return binding.root
    }
}