package architectgroup.udr.webserver.controller;

import architectgroup.fact.access.UserAccess;
import architectgroup.fact.access.util.FactAccessFactory;
import architectgroup.fact.dto.UserDto;
import architectgroup.udr.webserver.model.*;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Toan Dang, from Architect Group Inc.
 * User: Toandph
 * Date: 1/22/13
 * Time: 11:31 AM
 */
@Controller
@Scope("request")
public class UserController {
    @Autowired
    private SessionModel session;

    @Autowired
    private FactAccessFactory factAccess;

    @NotNull
    @RequestMapping(value = "/user/list")
    public ModelAndView list() {
        ModelAndView modelAndView = new ModelAndView("user/list");
        List<UserDto> userList;
        Map<String, Object> data;
        UserAccess userAccess;
        FactAccessFactory accessFactory;

        data  = new HashMap<String, Object>();
        userAccess = new UserAccess(factAccess);

        userList = userAccess.getUserList();
        data.put("userList", userList);

        modelAndView.addAllObjects(data);
        modelAndView.addObject("controller","user");
        modelAndView.addObject("action","list");

        return modelAndView;
    }

    @NotNull
    @RequestMapping(value = "/user/add")
    public ModelAndView add(@NotNull HttpServletRequest request, HttpServletResponse response, @NotNull @Valid @ModelAttribute("userModel") UserModel model, @NotNull BindingResult result) {
        ModelAndView modelAndView = new ModelAndView("user/add");

        if (request.getMethod().equalsIgnoreCase("GET")) {
            /* Initialize the model */
            UserModel newModel = new UserModel();
            modelAndView.addObject("userModel", newModel);
        } else {
            if (result.hasErrors()) {
                modelAndView.addObject("userModel", model);
            } else {
                UserAccess userAccess = new UserAccess(factAccess);
                if (model.getPassword().equals(model.getConfirmPassword())) {
                    if (userAccess.checkEmailExists(model.getEmail()) == null) {
                        userAccess.addUser(model.getUsername(), model.getPassword(), "No name", model.getEmail(), model.getRole());
                        return new ModelAndView("redirect:/user/list");
                    } else {
                        result.reject("email-already-exists");
                        modelAndView.addObject("userModel", model);
                    }
                } else {
                    result.reject("password-and-confirm-password-not-matched");
                    modelAndView.addObject("userModel", model);
                }
            }
        }

        modelAndView.addObject("controller","user");
        modelAndView.addObject("action","add");

        return modelAndView;
    }

    @NotNull
    @RequestMapping(value = "/user/edit")
    public ModelAndView edit(@NotNull HttpServletRequest request, HttpServletResponse response, @NotNull @RequestParam("userId") String username, @NotNull @Valid @ModelAttribute("userModel") UserModel model, @NotNull BindingResult result) {
        ModelAndView modelAndView = new ModelAndView("user/edit");
        UserAccess userAccess = new UserAccess(factAccess);

        if (request.getMethod().equalsIgnoreCase("GET")) {
            /* Initialize the model */
            UserModel newModel = new UserModel();
            UserDto userDto = userAccess.getUserDetail(username);
            newModel.setId(userDto.getId());
            newModel.setEmail(userDto.getEmail());
            newModel.setPassword(userDto.getPassword());
            newModel.setUsername(userDto.getUsername());
            newModel.setConfirmPassword(userDto.getPassword());
            newModel.setRole(userDto.getRole().getCode());
            modelAndView.addObject("userModel", newModel);
        } else {
            if (result.hasErrors()) {
                modelAndView.addObject("userModel", model);
            } else {
                if (!userAccess.checkUserExists(model.getUsername())) {
                    UserDto user = userAccess.checkEmailExists(model.getEmail());
                    if (user != null && user.getId() != model.getId()) {
                        result.reject("email-already-exists");
                        modelAndView.addObject("userModel", model);
                    } else {
                        userAccess.updateUser(model.getId(), model.getUsername(), model.getPassword(), "No name", model.getEmail(), model.getRole());
                        return new ModelAndView("redirect:/user/detail?id=" + model.getUsername());
                    }
                } else if (username.equalsIgnoreCase(model.getUsername())) {
                    UserDto user = userAccess.checkEmailExists(model.getEmail());
                    if (user != null && user.getId() != model.getId()) {
                        result.reject("email-already-exists");
                        modelAndView.addObject("userModel", model);
                    } else {
                        userAccess.updateUser(model.getId(), model.getUsername(), model.getPassword(), "No name", model.getEmail(), model.getRole());
                        return new ModelAndView("redirect:/user/detail?id=" + model.getUsername());
                    }
                } else {
                    result.reject("user-name-exist");
                    modelAndView.addObject("userModel", model);
                }
            }
        }

        modelAndView.addObject("controller","user");
        modelAndView.addObject("action","edit");

        return modelAndView;
    }

    @RequestMapping(value = "/user/login")
    public ModelAndView login(@NotNull HttpServletRequest request, HttpServletResponse response, @RequestParam(defaultValue = "0", value = "code") int code) {
        String referrer = request.getHeader("referer");
        ModelAndView modelAndView = new ModelAndView("user/login");
        if (code == 0) {
            modelAndView.addObject("message", "");
            session.setLastURL(referrer);
        } else if (code == 5) {     // Auto redirect
            modelAndView.addObject("message", "");
            session.setLastURL(null);
        } else{
            modelAndView.addObject("message", "wrong-username-or-password");
        }
        modelAndView.addObject("controller","user");
        modelAndView.addObject("action","login");
        return modelAndView;
    }

    @NotNull
    @RequestMapping(value = "/user/logout")
    public ModelAndView logout() {
        session.clearSession();
        return new ModelAndView("redirect:/project/outside-list");
    }

    @NotNull
    @RequestMapping(value = "/user/detail")
    public ModelAndView detail(@RequestParam("id") String username) {
        ModelAndView modelAndView = new ModelAndView("user/detail");

        UserAccess userAccess = new UserAccess(factAccess);
        UserModel detail = new UserModel();
        UserDto userDto = userAccess.getUserDetail(username);

        detail.setId(userDto.getId());
        detail.setEmail(userDto.getEmail());
        detail.setPassword(userDto.getPassword());
        detail.setUsername(userDto.getUsername());
        detail.setConfirmPassword(userDto.getPassword());
        detail.setRole(userDto.getRole().getCode());

        modelAndView.addObject("user", detail);

        modelAndView.addObject("controller","user");
        modelAndView.addObject("action","detail");

        return modelAndView;
    }

    @NotNull
    @RequestMapping(value = "/user/delete")
    public ModelAndView delete(@NotNull HttpServletRequest request, HttpServletResponse response) {
        ModelAndView view = new ModelAndView("redirect:/user/list");
        String[] userIds = request.getParameterValues("userId");
        for (String idStr : userIds) {
            UserAccess userAccess = new UserAccess(factAccess);
            int id = Integer.parseInt(idStr);
            if (userAccess.deleteUser(id) == -1) {
                view.addObject("error","there-is-a-project-created-by-this-user");
                break;
            }
        }
        return view;
    }

    @NotNull
    @RequestMapping(value = "/user/error")
    public ModelAndView error(@RequestParam("code") int code) {
        ModelAndView view = new ModelAndView("/user/error");
        String message = "";
        switch (code) {
            case 1:  message = "You are not authorized to view this page.";
                break;
        }
        view.addObject("message", message);
        return view;
    }

    /*
    @ExceptionHandler(Exception.class)
    public ModelAndView handleMyException(Exception exception) {
        ModelAndView mv = new ModelAndView("redirect:/project/outside-list");
        return mv;
    }
    */
}
