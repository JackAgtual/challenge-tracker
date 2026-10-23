import { components } from "@/types/api";
import { getFormErrorMessage } from "./form-utils";

describe("getFormErrorMessage", () => {
  it("returns detail from problem detail", () => {
    const problemDetail: components["schemas"]["ProblemDetail"] = {
      detail: "error 123",
    };

    expect(getFormErrorMessage(problemDetail)).toEqual({
      message: "error 123",
    });
  });

  it("uses default message if detail is undefined", () => {
    const problemDetail = {};

    expect(getFormErrorMessage(problemDetail)).toEqual({
      message: "Something went wrong",
    });
  });
});
