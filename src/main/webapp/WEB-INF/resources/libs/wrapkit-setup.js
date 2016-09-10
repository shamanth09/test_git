! function() {
    "use strict";
    var a = window.WrapkitHeader,
        b = document.querySelectorAll('[data-header="toggleSkin"]'),
        c = document.querySelectorAll('[data-header="toggleFixed"]'),
        d = document.querySelectorAll('[data-header="toggleRtl"]'),
        e = $.cookie("template_setups") ? $.parseJSON($.cookie("template_setups_header")) : {};
    window.wh = new a(".header", e), [].forEach.call(b, function(a) {
        var b = a.dataset.skin;
        a.addEventListener("click", function(a) {
            a.preventDefault(), window.wh.setSkin(b)
        })
    }), [].forEach.call(c, function(a) {
        a.addEventListener("click", function(b) {
            b.preventDefault();
            var c = a.dataset.position && "bottom" === a.dataset.position;
            c ? window.wh.fixedBottom() : window.wh.fixedTop()
        })
    }), [].forEach.call(d, function(a) {
        a.addEventListener("click", function(a) {
            a.preventDefault(), window.wh.rtl(!window.wh.options.rtlMode)
        })
    });
    var f = window.contextUrl+"resources/assets/images/logo/",
        g = document.querySelector(".logo"),
        h = window.wh.options.skin;
    "inverse" === h ? g.setAttribute("src", f + "brand-text-light.png") : g.setAttribute("src", f + "brand-text-dark.png"), window.wh.on("setSkin", function(a, b, c) {
        "inverse" === c ? g.setAttribute("src", f + "brand-text-light.png") : g.setAttribute("src", f + "brand-text-dark.png")
    }).on("fixedTop", function(a) {
        var b = a.$elem;
        b.find(".dropup").toggleClass("dropup dropdown")
    }).on("fixedBottom", function(a) {
        var b = a.$elem;
        b.find(".dropdown").toggleClass("dropdown dropup")
    }).on("rtl", function(a) {
        var b = a.$elem,
            c = b.find(".navbar-right"),
            d = b.find(".navbar-left"),
            e = b.find(".dropdown-menu-right"),
            f = b.find(".dropdown-menu-left");
        c.toggleClass("navbar-right navbar-left"), d.toggleClass("navbar-left navbar-right"), e.toggleClass("dropdown-menu-left dropdown-menu-right"), f.toggleClass("dropdown-menu-right dropdown-menu-left")
    }), $("#brandSearchNav").on("click", function(a) {
        a.preventDefault(), $("#brandSearchFrm").velocity("transition.expandIn", {
            duration: 250
        })
    }), $(".search-close").on("click", function() {
        $("#brandSearchFrm").velocity("reverse", {
            display: "none"
        })
    })
}(window),
function() {
    "use strict";
    var a = window.WrapkitSidebar,
        b = window.WrapkitUtils,
        c = document.querySelectorAll('[data-sidebar="toggleVisible"]'),
        d = document.querySelectorAll('[data-sidebar="toggleResize"]'),
        e = document.querySelectorAll('[data-sidebar="toggleCollapse"]'),
        f = document.querySelectorAll('[data-sidebar="toggleFixed"]'),
        g = document.querySelectorAll('[data-sidebar="toggleLoader"]'),
        h = document.querySelectorAll('[data-sidebar="toggleAlign"]'),
        i = document.querySelectorAll('[data-sidebar="toggleRtl"]'),
        j = document.querySelectorAll('[data-sidebar="toggleSkin"]'),
        k = document.querySelectorAll('[data-sidebar="toggleVariant"]'),
        l = document.querySelectorAll('[data-sidebar="toggleContext"]'),
        m = $.cookie("template_setups") ? $.parseJSON($.cookie("template_setups_sidebar")) : {
            mode: "vertical",
            skin: "dark",
            context: "teal"
        };
    window.sidebar = new a(".sidebar", m), [].forEach.call(c, function(a) {
        a.addEventListener("click", function(a) {
            a.preventDefault(), window.sidebar.options.visible ? window.sidebar.hide() : window.sidebar.show()
        })
    }), [].forEach.call(d, function(a) {
        a.addEventListener("click", function(a) {
            a.preventDefault(), window.sidebar.resizable(!window.sidebar.options.resizable)
        })
    }), [].forEach.call(e, function(a) {
        a.addEventListener("click", function(a) {
            a.preventDefault();
            var b = "lg" === window.sidebar.options.size ? "sm" : "lg";
            window.sidebar.size(b)
        })
    }), [].forEach.call(f, function(a) {
        a.addEventListener("click", function(a) {
            a.preventDefault(), window.sidebar.fixed(!window.sidebar.options.fixed)
        })
    }), [].forEach.call(g, function(a) {
        a.addEventListener("click", function(a) {
            a.preventDefault(), window.sidebar.showLoader(2), setTimeout(function() {
                window.sidebar.hideLoader(2)
            }, 3e3)
        })
    }), [].forEach.call(h, function(a) {
        a.addEventListener("click", function(a) {
            a.preventDefault();
            var b = "left" === window.sidebar.options.align ? "right" : "left";
            window.sidebar.align(b)
        })
    }), [].forEach.call(i, function(a) {
        a.addEventListener("click", function(a) {
            a.preventDefault(), window.sidebar.rtl(!window.sidebar.options.rtlMode)
        })
    }), [].forEach.call(j, function(a) {
        a.addEventListener("click", function(a) {
            a.preventDefault();
            var b = this.dataset.skin;
            window.sidebar.setSkin(b)
        })
    }), [].forEach.call(k, function(a) {
        a.addEventListener("click", function(a) {
            a.preventDefault();
            var b = this.dataset.variant;
            window.sidebar.setVariant(b)
        })
    }), [].forEach.call(l, function(a) {
        a.addEventListener("click", function(a) {
            a.preventDefault();
            var b = this.dataset.context;
            window.sidebar.setContext(b)
        })
    }), window.sidebar.on("_expand", function(a, b) {
        var c = $(b.querySelector(".nav-child"));
        $.Velocity.RunSequence([{
            e: c,
            p: "transition.fadeIn",
            o: {
                duration: 250
            }
        }, {
            e: c.children("li"),
            p: "transition.slideUpIn",
            o: {
                stagger: 35,
                sequenceQueue: !1
            }
        }])
    }).on("_collapse", function(a, b) {
        var c = $(b.querySelectorAll(".nav-child"));
        c.velocity("transition.fadeOut", {
            duration: 250
        })
    }).on("align", function(a, b) {
        "left" === b ? a.$elem.velocity("transition.slideLeftIn", {
            duration: 250
        }) : a.$elem.velocity("transition.slideRightIn", {
            duration: 250
        })
    }).on("setMode", function(a, c) {
        var d = a.$elem.find(".nav-wrapper");
        "vertical" === c ? a.options.fixed ? b.initSlimScroll(d) : (b.destroySlimScroll(d), d.css("height", "")) : b.viewport().width < 768 && a.options.fixed ? b.initSlimScroll(d) : (b.destroySlimScroll(d), d.css("height", ""))
    }).on("sticky", function() {
        var a = window.sidebar.$elem.find(".nav-wrapper");
        a.parent().hasClass("slimScrollDiv") && setTimeout(function() {
            var b = a.css("height");
            a.parent().css("height", b)
        }, 250)
    }).on("fixed", function(a, c) {
        var d = a.$elem.find(".nav-wrapper");
        ("vertical" === a.options.mode || b.viewport().width < 768 && "horizontal" === a.options.mode) && (c ? b.initSlimScroll(d) : b.destroySlimScroll(d))
    }), window.addEventListener("resize", b.debounce(function() {
        var a = window.sidebar.$elem.find(".nav-wrapper");
        "vertical" === window.sidebar.options.mode ? window.sidebar.options.fixed ? b.initSlimScroll(a) : (b.destroySlimScroll(a), a.css("height", "")) : b.viewport().width < 768 && window.sidebar.options.fixed ? b.initSlimScroll(a) : (b.destroySlimScroll(a), a.css("height", ""))
    }, 100))
}(window),
function() {
    "use strict";
    var a = window.WrapkitContent,
        b = document.querySelectorAll('[data-content="toggleRtl"]'),
        c = $.cookie("template_setups") ? $.parseJSON($.cookie("template_setups_content")) : {};
    window.wc = new a(".content-wrapper", c), [].forEach.call(b, function(a) {
        a.addEventListener("click", function(a) {
            a.preventDefault(), window.wc.rtl(!window.wc.options.rtlMode)
        })
    })
}(window),
function() {
    "use strict";
    var a = window.WrapkitLayout,
        b = window.WrapkitUtils,
        c = window.classie,
        d = document.querySelector(".header"),
        e = document.querySelector(".sidebar"),
        f = document.querySelector(".content-wrapper"),
        g = document.querySelectorAll('[data-layout="toggleBox"]'),
        h = document.querySelectorAll('[data-layout="toggleFs"]'),
        i = $.cookie("template_setups") ? $.parseJSON($.cookie("template_setups_layout")) : {};
    window.wl = new a(".wrapkit-wrapper", i), $.cookie("template_bg") ? (document.body.dataset.bg = $.cookie("template_bg"), c.add(document.body, "bg-grd-" + $.cookie("template_bg"))) : (document.body.dataset.bg = "dark", c.add(document.body, "bg-grd-dark")), [].forEach.call(g, function(a) {
        a.addEventListener("click", function(a) {
            a.preventDefault(), window.wl.options.box ? window.wl.setFluid() : window.wl.setBox()
        })
    }), [].forEach.call(h, function(a) {
        a.addEventListener("click", function(a) {
            a.preventDefault(), window.wl.options.fullscreen ? window.wl.exitFullscreen() : window.wl.fullscreen()
        })
    });
    var j = function() {
            var a = "right" === window.sidebar.options.align;
            window.scrollY > b.offsets(e).top ? "horizontal" === window.sidebar.options.mode ? (e.style.right = b.offsets(f).left + "px", e.style.left = b.offsets(f).left + "px") : a ? (e.style.right = b.offsets(f).left + "px", e.style.left = "") : (e.style.right = "", e.style.left = b.offsets(f).left + "px") : "horizontal" === window.sidebar.options.mode ? window.wh.options.fixed ? (e.style.right = b.offsets(f).left + "px", e.style.left = b.offsets(f).left + "px") : (e.style.right = "", e.style.left = "") : a ? (window.wh.options.fixed ? e.style.right = b.offsets(f).left + "px" : e.style.right = "", e.style.left = "") : (window.wh.options.fixed ? e.style.left = b.offsets(f).left + "px" : e.style.left = "", e.style.right = "")
        },
        k = function(a, c, d, e) {
            var f = window.sidebar.options.fixed;
            window.removeEventListener("scroll", j), e && f && b.viewport().width > 767 ? (j(), window.addEventListener("scroll", j)) : (a.style.right = "", a.style.left = ""), l()
        },
        l = function() {
            window.wl.options.box && window.wh.options.fixed && b.viewport().width > 767 ? (window.wh.elem.style.right = b.offsets(f).left + "px", window.wh.elem.style.left = b.offsets(f).left + "px") : (window.wl.options.box && window.wh.options.fixed && b.viewport().width <= 767 || window.wl.options.box && !window.wh.options.fixed || !window.wl.options.box) && (window.wh.elem.style.right = "", window.wh.elem.style.left = "")
        },
        m = function() {
            var a = window.wl.options.box;
            k(e, d, f, a)
        };
    l(), m(), window.wl.on("layoutChanged", function() {
        m()
    }), window.sidebar.on("fixed", m).on("align", m).on("setMode", m), window.wh.on("fixed", function() {
        l(), m()
    }), window.addEventListener("resize", function() {
        l(), m()
    })
}(window),
function() {
    "use strict";
    var a = window.WrapkitFooter,
        b = document.querySelectorAll('[data-footer="toggleSkin"]'),
        c = document.querySelectorAll('[data-footer="toggleRtl"]'),
        d = $.cookie("template_setups") ? $.parseJSON($.cookie("template_setups_footer")) : {};
    window.wf = new a(".footer-wrapper", d), [].forEach.call(b, function(a) {
        a.addEventListener("click", function(b) {
            b.preventDefault();
            var c = a.dataset.skin;
            window.wf.setSkin(c)
        })
    }), [].forEach.call(c, function(a) {
        a.addEventListener("click", function(a) {
            a.preventDefault(), window.wf.rtl(!window.wf.options.rtlMode)
        })
    })
}(window),
function() {
    "use strict";
    var a = window.WrapkitPanel,
        b = document.querySelectorAll(".content .panel");
    [].forEach.call(b, function(b) {
        new a(b, b.dataset)
    }), $(".panel").on("collapse", function(a, b) {
        var c = $(this),
            d = c.children(".panel-body, .panel-footer, .table, .list-group, .collapse-el");
        b ? (d.css("display", "block"), d.velocity("slideUp", {
            duration: 250,
            begin: function(a) {
                [].forEach.call(a, function(a) {
                    $(a).is("table") && $(a).css("display", "table")
                })
            }
        })) : d.css("display", "none").velocity("slideDown", {
            duration: 250,
            begin: function(a) {
                [].forEach.call(a, function(a) {
                    $(a).is("table") && $(a).css("opacity", 0)
                })
            },
            complete: function(a) {
                [].forEach.call(a, function(a) {
                    $(a).is("table") && $(a).css({
                        display: "table",
                        opacity: 1
                    })
                })
            }
        })
    }).on("expand", function(a, b) {
        var c = $(this);
        c.find('[data-toggle="tooltip"], [rel="tooltip"]').tooltip("hide"), b ? c.velocity("transition.expandIn", 250) : c.velocity.RunSequence([{
            e: c,
            p: "transition.expandOut",
            o: {
                duration: 250
            }
        }, {
            e: c,
            p: "transition.fadeIn",
            o: {
                duration: 300,
                sequenceQueue: !1
            }
        }])
    }), $(b).on("showLoader", function(a) {
        var b = a.target;
        setTimeout(function() {
            $(b).removeClass("panel-onloading"), $(b).find(".panel-title").removeClass("hide"), $(b).find(".panel-loader").remove()
        }, 1e3)
    })
}(window);