! function() {
    "use strict";
    var a = window.WrapkitUtils;
    [].forEach.call(document.querySelectorAll('[data-toggle="slimScroll"]'), function(b) {
        a.initSlimScroll($(b))
    }), $(document).on("click", ".stop-propagation", function(a) {
        a.stopPropagation()
    }).on("click", ".prevent-default", function(a) {
        a.preventDefault()
    }), $(document).on("focus", ".input-group-in .form-control", function() {
        var a = $(this).parent();
        a.hasClass("twitter-typeahead") ? a.parent().addClass("focus") : a.hasClass("input-group-in") && a.addClass("focus")
    }).on("blur", ".input-group-in .form-control", function() {
        var a = $(this).parent();
        a.hasClass("twitter-typeahead") ? a.parent().removeClass("focus") : a.hasClass("input-group-in") && a.removeClass("focus")
    }), $(document).on("focus", "label.select > select", function() {
        $(this).parent().addClass("focus")
    }).on("focusout", "label.select > select", function() {
        $(this).parent().removeClass("focus")
    }), $("#templateSetup").find(".modal-content").load(window.contextUrl+"resources/assets/_includes/setups.html", function() {
        var b = $('[data-target="#templateSetup"]'),
            c = b.data("scripts"),
            d = c ? c.replace(/\s+/g, "") : !1;
        d && (d = d.split(","), $.each(d, function(b, c) {
            a.createScript(c)
        }))
    }), $('[rel*="tooltip"], [data-toggle*="tooltip"]').each(function() {
        var a = $(this),
            b = a.data(),
            c = b.context ? "tooltip-" + b.context : "default";
        b.template = '<div class="tooltip ' + c + '"><div class="tooltip-arrow"></div><div class="tooltip-inner"></div></div>', a.tooltip(b)
    }), $('[data-toggle*="tooltip"][data-trigger-input], [rel*="tooltip"][data-trigger-input]').each(function() {
        var a = $(this),
            b = a.data(),
            c = b.triggerInput;
        $(document).on("focus", c, function() {
            a.tooltip("show")
        }).on("focusout", c, function() {
            a.tooltip("hide")
        })
    }), $(".disable-tooltip").tooltip("destroy"), $('[rel*="popover"], [data-toggle*="popover"]').each(function() {
        var a = $(this),
            b = a.data(),
            c = b.context ? "popover-" + b.context : "default";
        b.template = '<div class="popover ' + c + '"><div class="arrow"></div><div class="popover-inner"><h3 class="popover-title"></h3><div class="popover-content"><p></p></div></div></div>', a.popover(b)
    }), $('[data-toggle*="popover"][data-trigger-input], [rel*="popover"][data-trigger-input]').each(function() {
        var a = $(this),
            b = a.data(),
            c = b.triggerInput;
        $(document).on("focus", c, function() {
            a.popover("show")
        }).on("focusout", c, function() {
            a.popover("hide")
        })
    }), $(".disable-popover").popover("destroy"), $(document).on("loaded.bs.modal", ".modal", function(b) {
        var c = b.target.id,
            d = $('[data-target="#' + c + '"]'),
            e = d.data("scripts"),
            f = e ? e.replace(/\s+/g, "") : !1;
        f && (f = f.split(","), $.each(f, function(b, c) {
            a.createScript(c)
        }))
    }).on("hide.bs.modal", ".modal", function() {
        $(document).find("[data-toggle=popover], [rel*=popover]").popover("hide")
    }).on("shown.bs.modal", function(a) {
        var b = $("body > .modal-backdrop"),
            c = b.length > 1 ? !0 : !1;
        if (c) {
            var d = parseInt(b.first().css("z-index"));
            b.each(function() {
                var a = $(this);
                a.is(":first") || (d = parseInt(a.css("z-index"))), a.is(":last") || a.next().css("z-index", d + 10)
            });
            var e = parseInt(b.last().css("z-index"));
            $(a.target).css("z-index", e + 10)
        }
    }), $(".modal").on("show.bs.modal", function(a) {
        var b = $(a.target),
            c = b.data("transition");
        c && b.velocity("transition." + c, 500)
    }).on("hidden.bs.modal", function(a) {
        var b = $(a.target),
            c = b.data("transition");
        if (c) {
            var d = c.indexOf("In"),
                e = c.substring(0, d) + "Out";
            b.show().velocity("transition." + e, 500)
        }
    }), $(document).on("show.bs.dropdown", ".dropdown", function() {
        var a = $(this),
            b = a.children(".dropdown-menu"),
            c = b.children("li"),
            d = [{
                e: b,
                p: "transition.expandIn",
                o: {
                    duration: 250
                }
            }, {
                e: c,
                p: "transition.slideUpIn",
                o: {
                    stagger: 35,
                    sequenceQueue: !1
                }
            }];
        $.Velocity.RunSequence(d)
    }), $(document).on("hide.bs.dropdown", ".dropdown", function() {
        var a = $(this),
            b = a.children(".dropdown-menu");
        b.velocity("transition.slideUpOut", {
            duration: 250
        })
    }), $(document).on("show.bs.dropdown", ".dropup", function() {
        var a = $(this),
            b = a.children(".dropdown-menu"),
            c = b.children("li"),
            d = [{
                e: b,
                p: "transition.expandIn",
                o: {
                    duration: 250
                }
            }, {
                e: c,
                p: "transition.slideUpIn",
                o: {
                    stagger: 35,
                    backwards: !0,
                    sequenceQueue: !1
                }
            }];
        $.Velocity.RunSequence(d)
    }), $(document).on("hide.bs.dropdown", ".dropup", function() {
        var a = $(this),
            b = a.children(".dropdown-menu");
        b.velocity("transition.slideDownOut", {
            duration: 250
        })
    }), $(document).on("show.bs.dropdown", ".dropdown-ext", function() {
        var a = $(this),
            b = a.children(".dropdown-menu"),
            c = b.find(".media"),
            d = [{
                e: b,
                p: "transition.expandIn",
                o: {
                    duration: 250
                }
            }, {
                e: c,
                p: "transition.slideUpIn",
                o: {
                    stagger: 35,
                    sequenceQueue: !1
                }
            }];
        $.Velocity.RunSequence(d)
    }), $(document).on("hide.bs.dropdown", ".dropdown-ext", function() {
        var a = $(this),
            b = a.children(".dropdown-menu");
        b.velocity("transition.slideUpOut", {
            duration: 250
        })
    });
    var b = $(".dropdown-ext .dd-body");
    b.length && a.initSlimScroll(b, 360), $(".dropdown-menu-ext .dd-head, .dropdown-menu-ext .dd-actions").on("click", function(a) {
        a.stopPropagation()
    })
}(window),
function() {
    "use strict";
    $(".autogrow").autoGrow();
    var a = window.Switchery,
        b = document.querySelectorAll(".js-switch");
    [].forEach.call(b, function(b) {
        var c = b.dataset,
            d = {
                color: c.color ? c.color : "#48CFAD",
                jackColor: c.jackColor ? c.jackColor : "#ffffff",
                jackSecondaryColor: c.jackSecondaryColor ? c.jackSecondaryColor : "#CCD1D9",
                secondaryColor: c.secondaryColor ? c.secondaryColor : "#E6E9ED",
                className: c.className ? c.className : "switchery",
                disabled: c.disabled ? c.disabled : !1,
                disabledOpacity: c.disabledOpacity ? c.disabledOpacity : .5,
                speed: c.speed ? c.speed : "0.3s"
            };
        new a(b, d)
    })
}(window);